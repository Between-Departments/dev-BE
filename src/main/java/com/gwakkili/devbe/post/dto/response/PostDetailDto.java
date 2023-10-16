package com.gwakkili.devbe.post.dto.response;


import com.gwakkili.devbe.image.entity.PostImage;
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

    private String writer;

    private String writerImage;

    private String major;

    // ! Post.Category 접근 제어자 변경 : none(default private 인듯?) -> public
    private Post.Category category;

    private String title;

    private String content;

    private int viewCount;

    private int recommendCount;

    private List<String> images;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private boolean isAnonymous;


    public static PostDetailDto of(Post post){
        List<String> imageUrls = post.getImages().stream().map(PostImage::getUrl).collect(Collectors.toList());

        return PostDetailDto.builder()
                .postId(post.getPostId())
                .writer(post.getWriter().getNickname())
                .writerImage(post.getWriter().getImage().getThumbnailUrl())
                .major(post.getMajor())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .images(imageUrls)
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .isAnonymous(post.isAnonymous())
                .build();
    }
}
