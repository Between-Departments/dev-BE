package com.gwakkili.devbe.post.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostDetailDto {

    private long postId;

    private PostSimpleMemberDto writer;

    private Post.BoardType boardType;

    private Major.Category majorCategory;

    private Post.Tag tag;

    private String title;

    private String content;

    private long viewCount;

    private long recommendCount;

    private long replyCount;

    private List<String> images;

    private LocalDateTime createAt;

    private Boolean isAnonymous;

    private Boolean isMine;

    private Boolean isBookmarked;

    private Boolean isRecommended;

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
        List<String> images = post.getImages().stream().map(PostImage::getUrl).collect(Collectors.toList());

        return PostDetailDto.builder()
                .postId(post.getPostId())
                .writer(new PostSimpleMemberDto(post.getWriter(), post.getIsAnonymous()))
                .boardType(post.getBoardType())
                .majorCategory(post.getMajorCategory())
                .tag(post.getTag())
                .title(post.getTitle())
                .content(post.getContent())
                .images(images)
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .replyCount(post.getReplyCount())
                .createAt(post.getCreateAt())
                .isAnonymous(post.getIsAnonymous())
                .build();
    }

    public void setMine(Boolean mine) {
        isMine = mine;
    }

    public void setBookmarked(Boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public void setRecommended(Boolean recommended) {
        isRecommended = recommended;
    }
}
