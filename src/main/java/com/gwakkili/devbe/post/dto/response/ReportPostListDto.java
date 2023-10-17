package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ReportPostListDto extends AbstractPostListDto{

    private String writer;

    //    @Schema(description = "작성자 프로필 이미지 주소",example = "http://www.example.com/image_123123")
    private String writerImage;

    private long totalReportCount;

    public static ReportPostListDto of(Post post, long totalReportCount) {
        return ReportPostListDto.builder()
                .postId(post.getPostId())
                .writer(post.getWriter().getNickname())
                .writerImage(post.getWriter().getImage().getThumbnailUrl())
                .totalReportCount(totalReportCount)
                .title(post.getTitle())
                .content(post.getContent()) // ! unused in API
                .createAt(post.getCreateAt()) // ! unused in API
                .build();
    }


}
