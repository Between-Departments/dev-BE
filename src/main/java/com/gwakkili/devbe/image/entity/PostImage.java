package com.gwakkili.devbe.image.entity;

import com.gwakkili.devbe.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String url;

    @Builder
    public PostImage(Post post, String url) {
        this.post = post;
        this.url = url;
    }

    public String getThumbnailUrl() {
        return url.replace("/images/", "/thumbnails/");
    }

}
