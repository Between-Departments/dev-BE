package com.gwakkili.devbe.image.service;

import com.gwakkili.devbe.event.DeleteMemberImageEvent;
import com.gwakkili.devbe.event.DeletePostImageEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.UnsupportedException;
import com.gwakkili.devbe.image.entity.MemberImage;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ImageServiceImpl implements ImageService {

    private final String bucket;
    private final S3Client s3Client;
    private final S3Template s3Template;


    public ImageServiceImpl(@Value("${spring.cloud.aws.s3.bucket}") String bucket, S3Client s3Client, S3Template s3Template) {
        this.bucket = bucket;
        this.s3Client = s3Client;
        this.s3Template = s3Template;
    }

    @TransactionalEventListener
    public void deleteMemberImage(DeleteMemberImageEvent deleteMemberImageEvent) {
        String imageUrl = deleteMemberImageEvent.getImageUrl();

        if(!MemberImage.getDefaultImageUrl().equals(imageUrl)){
            deleteImage(imageUrl);
        }
    }

    @TransactionalEventListener
    public void deletePostImage(DeletePostImageEvent deletePostImageEvent) {
        List<String> imageUrls = deletePostImageEvent.getImageUrls();
        deleteImageList(imageUrls);
    }

    @Override
    public List<String> uploadImage(List<MultipartFile> multipartFiles) {
        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.getContentType().startsWith("image"))
                throw new UnsupportedException(ExceptionCode.UNSUPPORTED_MEDIA_TYPE);
            // 이미지와 썸네일 이미지 업로드 경로 생성
            String originalName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String imageUploadPath = "images/" + datePath + "/" + uuid + "_" + originalName;
            String thumbnailUploadPath = "thumbnails/" + datePath + "/" + uuid + "_" + originalName;

            //이미지와 섬네일 업로드
            try {
                String imageUrl = uploadImage(multipartFile, imageUploadPath);
                uploadThumbnailImage(multipartFile, thumbnailUploadPath);
                imageUrlList.add(imageUrl);
            } catch (IOException e) {
                throw new CustomException(ExceptionCode.S3_UPLOAD_FAIL);
            }

        }
        return imageUrlList;
    }

    private String uploadImage(MultipartFile multipartFile, String uploadPath) throws IOException {

        //메타 데이터 생성
        ObjectMetadata objectMetadata = ObjectMetadata.builder()
                .contentType(multipartFile.getContentType())
                .build();

        // S3에 폴더 및 파일 업로드
        InputStream inputStream = multipartFile.getInputStream();
        S3Resource uploaded = s3Template.upload(bucket, uploadPath, inputStream, objectMetadata);
        return uploaded.getURL().toString();
    }

    private void uploadThumbnailImage(MultipartFile multipartFile, String uploadPath) throws IOException {

        //썸네일 이미지 생성
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        BufferedImage thumbnailImage = Thumbnails.of(image).size(200, 200).asBufferedImage();
        ByteArrayOutputStream thumbnailOutStream = new ByteArrayOutputStream();
        String imageType = multipartFile.getContentType();
        ImageIO.write(thumbnailImage, imageType.substring(imageType.indexOf("/") + 1), thumbnailOutStream);

        // 메타 데이터 생성
        byte[] bytes = thumbnailOutStream.toByteArray();
        ObjectMetadata objectMetadata = ObjectMetadata.builder()
                .contentType(multipartFile.getContentType())
                .build();

        // s3에 이미지 저장
        InputStream thumbnailInput = new ByteArrayInputStream(bytes);
        s3Template.upload(bucket, uploadPath, thumbnailInput, objectMetadata);
    }

    @Override
    public void deleteImage(String imgUrl) {
        String splitStr = ".com/";
        String decodeUrl = URLDecoder.decode(imgUrl, StandardCharsets.UTF_8);
        String imagePath = decodeUrl.substring(imgUrl.lastIndexOf(splitStr) + splitStr.length());
        String thumbnailPath = imagePath.replace("images/", "thumbnails/");

        try {
            s3Template.deleteObject(bucket,imagePath);
            s3Template.deleteObject(bucket,thumbnailPath);
        }catch (SdkException exception){
            throw new CustomException(ExceptionCode.S3_DELETE_FAIL);
        }
    }

    @Override
    public void deleteImageList(List<String> imgUrlList) {
        List<ObjectIdentifier> objectIdentifierList = new ArrayList<>();

        imgUrlList.forEach(imgUrl -> {
            String splitStr = ".com/";
            String decodeUrl = URLDecoder.decode(imgUrl, StandardCharsets.UTF_8);
            String imagePath = decodeUrl.substring(imgUrl.lastIndexOf(splitStr) + splitStr.length());
            String thumbnailPath = imagePath.replace("images/", "thumbnails/");

            objectIdentifierList.add(ObjectIdentifier.builder().key(imagePath).build());
            objectIdentifierList.add(ObjectIdentifier.builder().key(thumbnailPath).build());
        });

        Delete delete = Delete.builder()
                .objects(objectIdentifierList)
                .quiet(false)
                .build();

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucket)
                .delete(delete)
                .build();

        try {
            s3Client.deleteObjects(deleteObjectsRequest);
        }catch (SdkException exception){
            throw new CustomException(ExceptionCode.S3_DELETE_FAIL);
        }
    }
}
