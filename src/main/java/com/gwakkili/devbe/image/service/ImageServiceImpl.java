package com.gwakkili.devbe.image.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gwakkili.devbe.event.DeleteMemberImageEvent;
import com.gwakkili.devbe.event.DeletePostImageEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.UnsupportedException;
import com.gwakkili.devbe.image.entity.MemberImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

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
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

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
                uploadImage(multipartFile, imageUploadPath);
                String thumbnailUrl = uploadThumbnailImage(multipartFile, thumbnailUploadPath);
                imageUrlList.add(thumbnailUrl);
            } catch (IOException e) {
                throw new CustomException(ExceptionCode.S3_UPLOAD_FAIL);
            }

        }
        return imageUrlList;
    }

    private void uploadImage(MultipartFile multipartFile, String uploadPath) throws IOException {

        //메타 데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        // S3에 폴더 및 파일 업로드
        InputStream inputStream = multipartFile.getInputStream();
        amazonS3.putObject(new PutObjectRequest(bucket, uploadPath, inputStream, objectMetadata));
    }

    private String uploadThumbnailImage(MultipartFile multipartFile, String uploadPath) throws IOException {

        //썸네일 이미지 생성
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        BufferedImage thumbnailImage = Thumbnails.of(image).size(200, 200).asBufferedImage();
        ByteArrayOutputStream thumbnailOutStream = new ByteArrayOutputStream();
        String imageType = multipartFile.getContentType();
        ImageIO.write(thumbnailImage, imageType.substring(imageType.indexOf("/") + 1), thumbnailOutStream);

        // 메타 데이터 생성
        ObjectMetadata thumbnailMetadata = new ObjectMetadata();
        byte[] bytes = thumbnailOutStream.toByteArray();
        thumbnailMetadata.setContentLength(bytes.length);
        thumbnailMetadata.setContentType(multipartFile.getContentType());

        // s3에 이미지 저장
        InputStream thumbnailInput = new ByteArrayInputStream(bytes);
        amazonS3.putObject(new PutObjectRequest(bucket, uploadPath, thumbnailInput, thumbnailMetadata));
        return amazonS3.getUrl(bucket, uploadPath).toString();

    }

    @Override
    public void deleteImage(String imgUrl) {
        String splitStr = ".com/";
        String decodeUrl = URLDecoder.decode(imgUrl, StandardCharsets.UTF_8);
        String imagePath = decodeUrl.substring(imgUrl.lastIndexOf(splitStr) + splitStr.length());
        String thumbnailPath = imagePath.replace("images/", "thumbnails/");

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, imagePath));
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, thumbnailPath));
        }catch (SdkClientException exception){
            throw new CustomException(ExceptionCode.S3_DELETE_FAIL);
        }
    }

    @Override
    public void deleteImageList(List<String> imgUrlList) {
        List<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
        for (String imgUrl : imgUrlList) {
            String splitStr = ".com/";
            String decodeUrl = URLDecoder.decode(imgUrl, StandardCharsets.UTF_8);
            String imagePath = decodeUrl.substring(imgUrl.lastIndexOf(splitStr) + splitStr.length());
            String thumbnailPath = imagePath.replace("images/", "thumbnails/");
            keyList.add(new DeleteObjectsRequest.KeyVersion(imagePath));
            keyList.add(new DeleteObjectsRequest.KeyVersion(thumbnailPath));
        }

        try {
            amazonS3.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(keyList).withQuiet(false));
        }catch (SdkClientException exception){
            throw new CustomException(ExceptionCode.S3_DELETE_FAIL);
        }
    }
}
