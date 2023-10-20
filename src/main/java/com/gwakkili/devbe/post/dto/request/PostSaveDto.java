package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Builder
public class PostSaveDto {

    @Length(min = 3, max = 30)
    private String title;

    @Length(min = 5, max = 1000)
    private String content;

    // ? 프로젝트에서 사용하고 있는 S3 서버의 url이 맞는지에 대한 검증이 필요한가?
    private List<String> imageUrls;

    // TODO Enum 값에 대한 Validation 필요
    // ! 1. 값이 아예 넘어오지 않거나, 빈문자열로 넘어오는 경우
    // ! 2. 존재하지 않는 Enum 값이 넘어오는 경우
    private Major.Category majorCategory;

    private Post.BoardType boardType;

    private Post.Tag tag;

    private Boolean isAnonymous;

}
