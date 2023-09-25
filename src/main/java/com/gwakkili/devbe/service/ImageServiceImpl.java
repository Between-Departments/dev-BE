package com.gwakkili.devbe.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public List<String> upload(List<MultipartFile> multipartFiles) throws IOException {


        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile multipartFile: multipartFiles){
            if(!multipartFile.getContentType().startsWith("image")){
                throw new CustomException(ExceptionCode.NOT_IMAGE);
            }

            String originalName = multipartFile.getOriginalFilename();
            String imageUri = getUploadPath(originalName);


            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {

                // S3에 폴더 및 파일 업로드
                amazonS3.putObject(
                        new PutObjectRequest(bucket, imageUri, inputStream, objectMetadata));


                // S3에 업로드한 image URL
                imageUrls.add(amazonS3.getUrl(bucket, imageUri).toString());

            } catch (IOException e) {
                throw new CustomException(ExceptionCode.FAIL_UPLOAD);
            }

        }
        return imageUrls;
    }

    private String getUploadPath(String originalName){
        String uuidImageName = UUID.randomUUID() + "_" + originalName;
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return "images/" + datePath + "/" +uuidImageName;
    }



    @Override
    public void delete(String imgUrl){
        String splitStr = ".com/";
        String fileName = imgUrl.substring(imgUrl.lastIndexOf(splitStr) + splitStr.length());
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}
