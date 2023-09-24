package com.gwakkili.devbe.entity.report;

import com.gwakkili.devbe.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostReport extends Report{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
