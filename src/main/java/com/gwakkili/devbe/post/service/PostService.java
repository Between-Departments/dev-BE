package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.dto.request.FreePostSaveDto;
import com.gwakkili.devbe.post.dto.request.NeedHelpPostSaveDto;
import com.gwakkili.devbe.post.dto.request.PostSearchCondition;
import com.gwakkili.devbe.post.dto.request.PostUpdateDto;
import com.gwakkili.devbe.post.dto.response.*;
import com.gwakkili.devbe.post.entity.Post;

import java.util.Set;

public interface PostService {

    PostDetailDto saveNewFreePost(FreePostSaveDto postSaveDto, Long memberId);

    PostDetailDto saveNewNeedHelpPost(NeedHelpPostSaveDto postSaveDto, Long memberId);

    void bookmarkPost(Long postId, Long memberId);

    void recommendPost(Long postId, Long memberId);

    void deletePost(Long postId, Long memberId, Set<Member.Role> roles);

    void updatePost(PostUpdateDto postUpdateDto, Long postId, Long memberId);

    PostDetailDto findPostDto(Long postId, Long memberId, Boolean doCountUp);

    SliceResponseDto<ReportPostListDto, Object[]> findReportedPostList(SliceRequestDto sliceRequestDto);

    SliceResponseDto<BasicPostListDto, Post> findPostList(SliceRequestDto sliceRequestDto, PostSearchCondition postSearchCondition);

    SliceResponseDto<MyPostListDto, Post> findMyPostList(SliceRequestDto sliceRequestDto, Long memberId, Post.BoardType boardType);

    SliceResponseDto<BookmarkPostListDto, Post> findBookmarkedPostList(SliceRequestDto sliceRequestDto, Long memberId, Post.BoardType boardType);

    ListResponseDto<BasicPostListDto, Post> findWeeklyHotPostList();

    ListResponseDto<BasicPostListDto, Object[]> findDailyHotPostList();
}
