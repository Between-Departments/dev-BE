package com.gwakkili.devbe.post.dto.response;


import com.gwakkili.devbe.dto.SimpleMemberDto;
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

    private SimpleMemberDto writer;

//    private String major;
//
//    private Post.Tag tag;

    private String title;

    private String content;

    private int viewCount;

    private int recommendCount;

    private int replyCount;

    // * 원본 이미지의 링크
    private List<String> images;

    private LocalDateTime createAt;

    private boolean isAnonymous;


    public static PostDetailDto of(Post post){
        List<String> imageUrls = post.getImages().stream().map(PostImage::getUrl).collect(Collectors.toList());

        return PostDetailDto.builder()
                .postId(post.getPostId())
                .writer(new SimpleMemberDto(post.getWriter(), post.isAnonymous()))
//                .major(post.getMajor())
//                .tag(post.getTag())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .replyCount(post.getReplyCount())
                .images(imageUrls)
                .createAt(post.getCreateAt())
                .isAnonymous(post.isAnonymous())
                .build();
    }
}
