package com.gwakkili.devbe.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BasicPostListDto extends AbstractPostListDto {


    private SimpleMemberDto writer;

    private boolean isAnonymous;

    public static BasicPostListDto of(Post post){
        List<String> thumbnailImages = post.getImages().stream().map(PostImage::getThumbnailUrl).collect(Collectors.toList());

        return BasicPostListDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent().length() > 40 ? post.getContent().substring(0,40) +"..." : post.getContent())
                .boardType(post.getBoardType())
                .majorCategory(Post.BoardType.NEED_HELP.equals(post.getBoardType()) ? post.getMajor() : null)
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .replyCount(post.getReplyCount())
                .createAt(post.getCreateAt())
                .writer(Post.BoardType.NEED_HELP.equals(post.getBoardType()) ? null : new SimpleMemberDto(post.getWriter(), post.getIsAnonymous()))
                .thumbnailImages(thumbnailImages)
                .isAnonymous(post.getIsAnonymous())
                .build();
    }

    public static BasicPostListDto of(Long postId, String title, String majorCategory, LocalDateTime createAt) {
        return BasicPostListDto.builder()
                .postId(postId)
                .title(title)
                .majorCategory(majorCategory)
                .createAt(createAt)
                .build();
    }
}