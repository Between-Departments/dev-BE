package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
public class BookmarkPostListDto extends AbstractPostListDto{

    private String writer;

    private String writerImage;

    private int viewCount;

    private int recommendCount;

    private boolean isAnonymous;

    private List<String> thumbnailImages;

    public static BookmarkPostListDto of(PostBookmark postBookmark) {
        Post post = postBookmark.getPost();
        List<String> thumbnailImages = post.getImages().stream().map(postImage -> postImage.getThumbnailUrl()).collect(Collectors.toList());

        return BookmarkPostListDto.builder()
                .postId(post.getPostId())
                .writer(post.getWriter().getNickname())
                .writerImage(post.getWriter().getImage().getThumbnailUrl())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .thumbnailImages(thumbnailImages)
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .isAnonymous(post.isAnonymous())
                .build();
    }

}
