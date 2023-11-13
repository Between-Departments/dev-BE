package com.gwakkili.devbe.post.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class MyPostListDto extends AbstractPostListDto{

    @JsonIgnore // ! 테스트 확인용 작성자 본인 아이디
    private Long writerId;

    public static MyPostListDto of(Post post){
        List<String> thumbnailImages = post.getImages().stream().map(PostImage::getThumbnailUrl).collect(Collectors.toList());

        return MyPostListDto.builder()
                .writerId(post.getWriter().getMemberId())
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent().length() > 40 ? post.getContent().substring(0,40) +"..." : post.getContent())
                .majorCategory(Post.BoardType.NEED_HELP.equals(post.getBoardType()) ? post.getMajorCategory() : null)
                .tag(Post.BoardType.FREE.equals(post.getBoardType())? post.getTag() : null)
                .thumbnailImages(thumbnailImages)
                .viewCount(post.getViewCount())
                .recommendCount(post.getRecommendCount())
                .replyCount(post.getReplyCount())
                .createAt(post.getCreateAt())
                .build();
    }
}