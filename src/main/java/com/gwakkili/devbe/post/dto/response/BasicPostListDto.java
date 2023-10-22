package com.gwakkili.devbe.post.dto.response;

import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class BasicPostListDto extends AbstractPostListDto {

    private SimpleMemberDto writer;

    private boolean isAnonymous;

    public static BasicPostListDto of(Post post){
        List<String> thumbnailImages = post.getImages().stream().map(PostImage::getThumbnailUrl).collect(Collectors.toList());

        return BasicPostListDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent().length() > 40 ? post.getContent().substring(0,40) +"..." : post.getContent())
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