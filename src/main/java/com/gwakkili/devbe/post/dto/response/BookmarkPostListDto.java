package com.gwakkili.devbe.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookmarkPostListDto extends AbstractPostListDto{

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private SimpleMemberDto writer;

    private boolean isAnonymous;


    public static BookmarkPostListDto of(Post post) {
        List<String> thumbnailImages = post.getImages().stream().map(PostImage::getThumbnailUrl).collect(Collectors.toList());

        return BookmarkPostListDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent().length() > 40 ? post.getContent().substring(0,40) +"..." : post.getContent())
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
}