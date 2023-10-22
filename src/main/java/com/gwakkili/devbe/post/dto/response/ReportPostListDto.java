package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportPostListDto {

    private long postId;

    private SimpleMemberDto writer;

    private long totalReportCount;

    private String title;

    public static ReportPostListDto of(Post post, long totalReportCount) {
        return ReportPostListDto.builder()
                .postId(post.getPostId())
                .writer(new SimpleMemberDto(post.getWriter(), false))
                .totalReportCount(totalReportCount)
                .title(post.getTitle())
                .build();
    }


}
