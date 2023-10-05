package com.gwakkili.devbe.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public List<String> upload(List<MultipartFile> multipartFiles) throws IOException {


        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            // 이미지와 썸네일 이미지 업로드 경로 생성
            String originalName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String imageUploadPath = "images/" + datePath + "/" + uuid + "_" + originalName;
            String thumbnailUploadPath = "thumbnails/" + datePath + "/" + uuid + "_" + originalName;

            //이미지와 섬네일 업로드
            String imageUrl = uploadImage(multipartFile, imageUploadPath);
            uploadThumbnailImage(multipartFile, thumbnailUploadPath);

            imageUrlList.add(imageUrl);
        }
        return imageUrlList;
    }

    private String uploadImage(MultipartFile multipartFile, String uploadPath) {

        //메타 데이터 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            // S3에 폴더 및 파일 업로드
            amazonS3.putObject(new PutObjectRequest(bucket, uploadPath, inputStream, objectMetadata));
            log.info(amazonS3.getUrl(bucket, uploadPath).toString());
            return amazonS3.getUrl(bucket, uploadPath).toString();
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.FAIL_UPLOAD);
        }
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
        try (InputStream thumbnailInput = new ByteArrayInputStream(bytes);) {
            amazonS3.putObject(new PutObjectRequest(bucket, uploadPath, thumbnailInput, thumbnailMetadata));
            log.info(amazonS3.getUrl(bucket, uploadPath).toString());
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.FAIL_UPLOAD);
        }
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
}
