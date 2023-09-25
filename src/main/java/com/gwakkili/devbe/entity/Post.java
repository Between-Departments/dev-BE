package com.gwakkili.devbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id",nullable = false)

    private Member writer;

    @Enumerated(EnumType.STRING)
    private Major.Category majorCategory;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private int viewCount;

    private int recommendCount;

    @Builder
    public Post(long postId, Member writer, Major.Category majorCategory, Category category, String title, String content) {
        this.postId = postId;
        this.writer = writer;
        this.majorCategory = majorCategory;
        this.category = category;
        this.title = title;
        this.content = content;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addViewCount() {
        this.viewCount++;
    }

    public void addRecommendCount() {
        this.recommendCount++;
    }


    @RequiredArgsConstructor
    enum Category{
        NEED_HELP("도움이 필요해요"),
        HOBBY("취미"),
        LOVE("언애"),
        DAILY("일상"),
        TOGETHER("같이해요"),
        RESTAURANT("맛집"),
        CHITCHAT("잡담");

        private final String description;
    }

}
