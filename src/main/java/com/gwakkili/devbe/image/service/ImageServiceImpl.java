package com.gwakkili.devbe.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gwakkili.devbe.event.DeleteMemberEvent;
import com.gwakkili.devbe.event.DeletePostEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.UnsupportedException;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.image.repository.MemberImageRepository;
import com.gwakkili.devbe.image.repository.PostImageRepository;
import com.gwakkili.devbe.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private final MemberImageRepository memberImageRepository;

    private final PostImageRepository postImageRepository;

    @EventListener
    public void deleteMemberImage(DeleteMemberEvent deleteMemberEvent) {
        memberImageRepository.
                findByMember(deleteMemberEvent.getMember()).ifPresent(memberImage -> {
                    //delete(memberImage.getUrl()); // s3 이미지 삭제
                    memberImageRepository.delete(memberImage);
                }
        );
    }

    @EventListener
    public void deletePostImage(DeletePostEvent deletePostEvent) {
        List<Post> postList = deletePostEvent.getPostList();
        List<PostImage> postImageList = postImageRepository.findByPostIn(postList);
        List<String> imgUrlList = postImageList.stream().map(PostImage::getUrl).collect(Collectors.toList());
        //deleteAll(imgUrlList);
        postImageRepository.deleteAllInBatch(postImageList);
    }

    @Override
    public List<String> upload(List<MultipartFile> multipartFiles) {


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
                throw new CustomException(ExceptionCode.FAIL_UPLOAD);
            }

        }
        return imageUrlList;
    }

    private String uploadImage(MultipartFile multipartFile, String uploadPath) throws IOException {

        //메타 데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        // S3에 폴더 및 파일 업로드
        InputStream inputStream = multipartFile.getInputStream();
        amazonS3.putObject(new PutObjectRequest(bucket, uploadPath, inputStream, objectMetadata));
        return amazonS3.getUrl(bucket, uploadPath).toString();
    }

    private void uploadThumbnailImage(MultipartFile multipartFile, String uploadPath) throws IOException {

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

    }

    @Override
    public void delete(String imgUrl) {
        String splitStr = ".com/";
        String decodeUrl = URLDecoder.decode(imgUrl, StandardCharsets.UTF_8);
        String imagePath = decodeUrl.substring(imgUrl.lastIndexOf(splitStr) + splitStr.length());
        String thumbnailPath = imagePath.replace("images/", "thumbnails/");
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, imagePath));
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, thumbnailPath));
    }

    public void deleteAll(List<String> imgUrlList) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
        List<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
        for (String imgUrl : imgUrlList) {
            String splitStr = ".com/";
            String decodeUrl = URLDecoder.decode(imgUrl, StandardCharsets.UTF_8);
            String imagePath = decodeUrl.substring(imgUrl.lastIndexOf(splitStr) + splitStr.length());
            String thumbnailPath = imagePath.replace("images/", "thumbnails/");
            keyList.add(new DeleteObjectsRequest.KeyVersion(imagePath));
            keyList.add(new DeleteObjectsRequest.KeyVersion(thumbnailPath));
        }
        deleteObjectsRequest.setKeys(keyList);
        amazonS3.deleteObjects(deleteObjectsRequest);
    }
}
