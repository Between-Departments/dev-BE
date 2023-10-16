package com.gwakkili.devbe.report.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.report.entity.PostReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    Optional<PostReport> findByReporterAndPost(Member reporter, Post findPost);

    @EntityGraph(attributePaths = {"reporter"})
    Slice<PostReport> findByPost(Post post, Pageable pageable);
}
