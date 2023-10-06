package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.post.dto.response.BookmarkPostListDto;
import com.gwakkili.devbe.post.dto.response.MyPostListDto;
import com.gwakkili.devbe.post.dto.response.PostDetailDto;
import com.gwakkili.devbe.post.dto.request.PostReportDto;
import com.gwakkili.devbe.post.dto.request.PostSaveDto;
import com.gwakkili.devbe.post.dto.request.PostUpdateDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import com.gwakkili.devbe.report.entity.PostReport;

public interface PostService {

    void saveNewPost(PostSaveDto postSaveDto, long memberId);

    void reportPost(PostReportDto postReportDto, Long postId, long memberId);

    void bookmarkPost(Long postId, long memberId);

    void recommendPost(Long postId, long memberId);

    void deletePost(Long postId, long memberId);

    void updatePost(PostUpdateDto postUpdateDto, Long postId, long memberId);

    Post find(Long postId);

    PostDetailDto findPostDto(Long postId);

    SliceResponseDto<PostDetailDto, PostReport> findReportedPostList(SliceRequestDto sliceRequestDto);

    SliceResponseDto<PostDetailDto, Post> findPostList(SliceRequestDto sliceRequestDto);

    SliceResponseDto<MyPostListDto, Post> findMyPostList(SliceRequestDto sliceRequestDto, long memberId);

    SliceResponseDto<BookmarkPostListDto, PostBookmark> findBookmarkedPostList(SliceRequestDto sliceRequestDto, long memberId);
}
