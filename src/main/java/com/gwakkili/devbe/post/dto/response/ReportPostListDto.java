package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportPostListDto {

    private Long postId;

    private SimpleMemberDto writer;

    private Long totalReportCount;

    private String title;

    public static ReportPostListDto of(Post post, Long totalReportCount) {
        return ReportPostListDto.builder()
                .postId(post.getPostId())
                .writer(new SimpleMemberDto(post.getWriter(), false))
                .totalReportCount(totalReportCount)
                .title(post.getTitle())
                .build();
    }


}
