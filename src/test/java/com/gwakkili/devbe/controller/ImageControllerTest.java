package com.gwakkili.devbe.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.gwakkili.devbe.config.S3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Import(S3MockConfig.class)
@ActiveProfiles("test")
public class ImageControllerTest {


    @Autowired
    MockMvc mockMvc;

    private static final String BUCKET_NAME = "test-bucket";

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

    private MockMultipartFile getMultipartFile(String name, String originalName) throws IOException {
        return new MockMultipartFile(name,
                originalName, "image/jpg",
                new FileInputStream("src/test/resources/test-image/test.jpg"));

    }

    @Test
    @DisplayName("이미지 업로드 테스트")
    public void imageUploadTest() throws Exception {

        //given
        String url = "/images";
        MockMultipartFile image1 = getMultipartFile("images", "test1.jpg");
        MockMultipartFile image2 = getMultipartFile("images", "test2.jpg");

        mockMvc.perform(multipart(url)
                .file(image1).file(image2))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
