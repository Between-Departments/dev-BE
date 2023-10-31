package com.gwakkili.devbe.post.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id",nullable = false)
    private Member writer;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Enumerated(EnumType.STRING)
    private Major.Category majorCategory;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<PostImage> images = new ArrayList<>();

    // TODO 어떻게 처리할 것인가에 대한 방법 논의 필요
    private long viewCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM post_recommend pr WHERE pr.post_id = post_id)")
    private long recommendCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM reply r WHERE r.post_id = post_id)")
    private long replyCount;

    private Boolean isAnonymous;

    @Builder
    public Post(Member writer, BoardType boardType, Major.Category majorCategory, Tag tag, String title, String content, Boolean isAnonymous) {
        this.writer = writer;
        this.boardType = boardType;
        this.majorCategory = majorCategory;
        this.tag = tag;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void setRecommendCount(long recommendCount) {
        this.recommendCount = recommendCount;
    }

    public void setReplyCount(long replyCount) {
        this.replyCount = replyCount;
    }

    public void addImages(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            PostImage newPostImage = PostImage.builder()
                    .post(this)
                    .url(imageUrl)
                    .build();

            this.getImages().add(newPostImage);
        }
    }

    public void update(String title, String content, Major.Category majorCategory, Tag tag, Boolean isAnonymous, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.majorCategory = majorCategory;
        this.tag = tag;
        this.isAnonymous = isAnonymous;

        if(imageUrls != null) updateImages(imageUrls);
    }


    private void updateImages(List<String> imageUrls) {
        ArrayList<PostImage> updatedImages = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            PostImage newPostImage = PostImage.builder()
                    .post(this)
                    .url(imageUrl)
                    .build();
            updatedImages.add(newPostImage);
        }

        this.images = updatedImages;
    }

    @RequiredArgsConstructor
    public enum BoardType{
        NEED_HELP("도움이 필요해요"),
        FREE("자유게시판");

        private final String description;

        @JsonCreator
        public static BoardType from(String val){
            return Arrays.stream(values())
                    .filter(boardType -> String.valueOf(boardType).equals(val))
                    .findAny()
                    .orElse(null);
        }
    }


    @RequiredArgsConstructor
    public enum Tag {
        HOBBY("취미"),
        LOVE("연애"),
        DAILY("일상"),
        TOGETHER("같이해요"),
        RESTAURANT("맛집"),
        CHITCHAT("잡담");

        private final String description;

        @JsonCreator
        public static Tag from(String val){
            return Arrays.stream(values())
                    .filter(tag -> String.valueOf(tag).equals(val))
                    .findAny()
                    .orElse(null);
        }
    }
}
