package com.gwakkili.devbe.post.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostDetailDto {

    private long postId;

    private PostSimpleMemberDto writer;

    private Post.BoardType boardType;

    private String title;

    private String content;

    private long viewCount;

    private long recommendCount;

    private long replyCount;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> images;

    private LocalDateTime createAt;

    private Boolean isAnonymous;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    static class PostSimpleMemberDto extends SimpleMemberDto{

        private String school;
        private String major;

        private PostSimpleMemberDto(Member member, Boolean isAnonymous) {
            super(member, isAnonymous);
            this.school = isAnonymous ? null : member.getSchool();
            this.major = isAnonymous ? null : member.getMajor();
        }
    }

    public static PostDetailDto of(Post post){
        List<String> imageUrls = post.getImages().stream().map(PostImage::getUrl).collect(Collectors.toList());

        return PostDetailDto.builder()
                .postId(post.getPostId())
                .writer(new PostSimpleMemberDto(post.getWriter(), post.getIsAnonymous()))
                .boardType(post.getBoardType())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .replyCount(post.getReplyCount())
                .images(imageUrls)
                .createAt(post.getCreateAt())
                .isAnonymous(post.getIsAnonymous())
                .build();
    }
}
