package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class MyPostListDto extends AbstractPostListDto{

    public static MyPostListDto of(Post post){
        List<String> thumbnailImages = post.getImages().stream().map(PostImage::getThumbnailUrl).collect(Collectors.toList());

        return MyPostListDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent().length() > 40 ? post.getContent().substring(0,40) +"..." : post.getContent())
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .thumbnailImages(thumbnailImages)
                .createAt(post.getCreateAt())
                .build();
    }
}