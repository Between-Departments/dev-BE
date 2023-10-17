package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class BookmarkPostListDto extends AbstractPostListDto{

    private SimpleMemberDto writer;

    private boolean isAnonymous;

    private List<String> thumbnailImages;

    public static BookmarkPostListDto of(PostBookmark postBookmark) {
        Post post = postBookmark.getPost();
        List<String> thumbnailImages = post.getImages().stream().map(PostImage::getThumbnailUrl).collect(Collectors.toList());

        return BookmarkPostListDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent().substring(0,40) +"...")
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .replyCount(post.getReplyCount())
                .createAt(post.getCreateAt())
                .writer(new SimpleMemberDto(post.getWriter(), post.isAnonymous()))
                .thumbnailImages(thumbnailImages)
                .isAnonymous(post.isAnonymous())
                .build();
    }
}