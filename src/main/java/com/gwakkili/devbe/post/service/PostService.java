package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.post.dto.PostDto;
import com.gwakkili.devbe.post.dto.PostReportDto;
import com.gwakkili.devbe.post.dto.PostSaveDto;
import com.gwakkili.devbe.post.dto.PostUpdateDto;
import com.gwakkili.devbe.post.entity.Post;

public interface PostService {

    void saveNewPost(PostSaveDto postSaveDto, long memberId);

    void reportPost(PostReportDto postReportDto, Long postId, long memberId);

    void bookmarkPost(Long postId, long memberId);

    void recommendPost(Long postId, long memberId);

    void deletePost(Long postId, long memberId);

    void updatePost(PostUpdateDto postUpdateDto, Long postId, long memberId);

    Post find(Long postId);

    PostDto findPostDto(Long postId);

    void findReportedPostList();

    void findPostList();

    void findMyPostList(long memberId);

    void findBookmarkedPostList(long memberId);
}
