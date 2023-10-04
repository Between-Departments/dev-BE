package com.gwakkili.devbe.image.entity;

import com.gwakkili.devbe.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String url;

    public String getThumbnailUrl() {
        return url.replace("/images/", "/thumbnails/");
    }

}
