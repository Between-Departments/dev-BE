package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class BookmarkPostListDto extends AbstractPostListDto{

    private String writer;

//    @Schema(description = "작성자 프로필 이미지 주소",example = "http://www.example.com/image_123123")
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
