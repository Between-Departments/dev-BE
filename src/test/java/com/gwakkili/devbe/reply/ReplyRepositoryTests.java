package com.gwakkili.devbe.reply;

import com.gwakkili.devbe.config.DummyDataProvider;
import com.gwakkili.devbe.reply.repository.ReplyRepository;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(DummyDataProvider.class)
@ActiveProfiles("test")
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void findReported() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Slice<Object[]> reportedReply = replyRepository.findReported(pageable);
        //then
        reportedReply.stream().forEach(tuple -> System.out.println(tuple[1]));
    }

}
