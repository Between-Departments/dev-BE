package com.gwakkili.devbe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post extends BaseEntity{
    enum Category{

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nickname", nullable = false)
    private Member writer;

    @Enumerated(EnumType.STRING)
    private Major.Category majorCategory;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String title;

    private String content;

    private int viewCount;

    private int recommendCount;
}
