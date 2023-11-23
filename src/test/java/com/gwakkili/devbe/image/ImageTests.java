package com.gwakkili.devbe.image;

import com.gwakkili.devbe.DevBeApplicationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageTests extends DevBeApplicationTests {


    private static final String BUCKET_NAME = "test-bucket";

//    @BeforeAll
//    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
//        s3Mock.start();
//        amazonS3.createBucket(BUCKET_NAME);
//    }
//
//    @AfterAll
//    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
//        amazonS3.shutdown();
//        s3Mock.stop();
//    }

    private MockMultipartFile getMultipartFile(String name, String originalName) throws IOException {
        return new MockMultipartFile(name,
                originalName, "image/jpg",
                new FileInputStream("src/test/resources/test-image/test.jpg"));

    }

    @Test
    @DisplayName("이미지 업로드 테스트")
    public void imageUploadTest() throws Exception {
        //given
        String url = "/api/images";
        MockMultipartFile image1 = getMultipartFile("images", "test1.jpg");
        MockMultipartFile image2 = getMultipartFile("images", "test2.jpg");

        mockMvc.perform(multipart(url)
                .file(image1).file(image2))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    public void imageDeleteTest() throws Exception {
        //given
    }
}
