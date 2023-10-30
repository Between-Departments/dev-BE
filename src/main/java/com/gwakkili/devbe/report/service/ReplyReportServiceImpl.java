package com.gwakkili.devbe.report.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.event.DeleteMemberEvent;
import com.gwakkili.devbe.event.DeleteReplyEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.repository.ReplyRepository;
import com.gwakkili.devbe.report.dto.response.ReplyReportDto;
import com.gwakkili.devbe.report.dto.request.ReplyReportSaveDto;
import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.report.repository.ReplyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyReportServiceImpl implements ReplyReportService {

    private final ReplyReportRepository replyReportRepository;

    private final ReplyRepository replyRepository;

    private final MemberRepository memberRepository;


    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ReplyReportDto, ReplyReport> getReplyReportList(long replyId, SliceRequestDto sliceRequestDto) {
        Reply reply = replyRepository.getReferenceById(replyId);
        Slice<ReplyReport> replyReportList = replyReportRepository.findByReply(reply, sliceRequestDto.getPageable());
        if (replyReportList.getNumberOfElements() == 0) throw new NotFoundException(ExceptionCode.NOT_FOUND_REPLY);
        Function<ReplyReport, ReplyReportDto> fn = (ReplyReportDto::of);
        return new SliceResponseDto(replyReportList, fn);
    }

    @Override
    public void saveReplyReport(ReplyReportSaveDto replyReportSaveDto) {

        Reply reply = replyRepository.getReferenceById(replyReportSaveDto.getReplyId());
        Member reporter = memberRepository.getReferenceById(replyReportSaveDto.getMemberId());

        if (replyReportRepository.existsByReporterAndReply(reporter, reply)) {
            throw new CustomException(ExceptionCode.DUPLICATE_REPORT);
        } else {
            ReplyReport replyReport = ReplyReport.builder()
                    .reply(reply)
                    .reporter(reporter)
                    .type(replyReportSaveDto.getType())
                    .content(replyReportSaveDto.getContent())
                    .build();

            replyReportRepository.save(replyReport);
        }
    }

    @Override
    public void deleteReplyReport(long reportId) {
        replyReportRepository.deleteById(reportId);
    }

    @EventListener
    public void deleteReplyReport(DeleteReplyEvent deleteReplyEvent) {
        replyReportRepository.deleteByReplyIn(deleteReplyEvent.getReplyList());
    }

    @EventListener
    public void deleteReplyReport(DeleteMemberEvent deleteMemberEvent) {
        replyReportRepository.deleteByReporter(deleteMemberEvent.getMember());
    }
}
