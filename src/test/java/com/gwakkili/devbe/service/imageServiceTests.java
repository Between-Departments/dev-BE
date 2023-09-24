package com.gwakkili.devbe.service;

import com.amazonaws.services.s3.AmazonS3;
import com.gwakkili.devbe.config.S3MockConfig;
import io.findify.s3mock.S3Mock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;


import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Import(S3MockConfig.class)
@ActiveProfiles("test")
@SpringBootTest
public class imageServiceTests {


    @Autowired
    private ImageService imageService;

    @Autowired
    private static final String BUCKET_NAME = "test.bucket";

    @BeforeAll
    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        s3Mock.start();
        amazonS3.createBucket(BUCKET_NAME);
    }

    @AfterAll
    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        amazonS3.shutdown();
        s3Mock.stop();
    }
    private MockMultipartFile[] getMultipartFiles() throws IOException {
        MockMultipartFile[] multipartFiles = new MockMultipartFile[3];
        for(int i = 0 ; i< 3; i++){
            MockMultipartFile mockMultipartFile = new MockMultipartFile("image"+i,
                    "test"+i+".jpg", "image/jpg",
                    new FileInputStream("src/test/resources/test-image/test.jpg"));
            multipartFiles[i] = mockMultipartFile;
        }
        return multipartFiles;
    }

    @Test
    @DisplayName("이미지 업로드 테스트")
    public void imageUploadTest() throws IOException {
        //given
        MockMultipartFile[] multipartFiles = getMultipartFiles();
        //when
        List<String> imageUrls = imageService.upload(multipartFiles);
        //then
        int i = 0;
        for(String imageUrl: imageUrls){
            Assertions.assertThat(imageUrl).contains("images/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            Assertions.assertThat(imageUrl).contains("test" + i + ".jpg");
            i++;
        }
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    public void imageDeleteTest() throws Exception {
        //given
        MockMultipartFile[] multipartFiles = getMultipartFiles();
        List<String> imageUrls = imageService.upload(multipartFiles);

        //when, then
        imageUrls.forEach(imageUrl-> {
            imageService.delete(imageUrl);
        });

    }

}
