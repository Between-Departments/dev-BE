package com.gwakkili.devbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.config.DummyDataProvider;
import com.gwakkili.devbe.config.S3MockConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import({S3MockConfig.class, DummyDataProvider.class})
@ActiveProfiles("test")
public class DevBeApplicationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
