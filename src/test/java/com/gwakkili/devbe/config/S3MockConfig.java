package com.gwakkili.devbe.config;

import io.findify.s3mock.S3Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class S3MockConfig {

    @Bean(name = "s3Mock")
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    }

}
