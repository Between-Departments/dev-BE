package com.gwakkili.devbe.image.dto;

import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.image.entity.PostImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class ImageDto {

    @NotBlank
    @URL
    @Schema(description = "이미지 url")
    private String imageUrl;

    @NotBlank
    @URL
    @Schema(description = "썸네일 이미지 url")
    private String thumbnailUrl;

    public MemberImage dtoToMemberImage() {
        return MemberImage.builder()
                .imageUrl(imageUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

    public PostImage dtoToPostImage() {
        return PostImage.builder()
                .imageUrl(imageUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

}
