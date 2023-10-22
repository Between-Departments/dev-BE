package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.dto.request.*;
import com.gwakkili.devbe.post.dto.response.*;
import com.gwakkili.devbe.post.entity.Post;

import java.util.Set;

public interface PostService {

    PostDetailDto saveNewFreePost(FreePostSaveDto postSaveDto, long memberId);

    PostDetailDto saveNewNeedHelpPost(NeedHelpPostSaveDto postSaveDto, long memberId);

    void bookmarkPost(Long postId, long memberId);

    void recommendPost(Long postId, long memberId);

    void deletePost(Long postId, long memberId, Set<Member.Role> roles);

    void updatePost(PostUpdateDto postUpdateDto, Long postId, long memberId);

    PostDetailDto findPostDto(Long postId);

    SliceResponseDto<ReportPostListDto, Object[]> findReportedPostList(SliceRequestDto sliceRequestDto);

    SliceResponseDto<BasicPostListDto, Post> findPostList(SliceRequestDto sliceRequestDto, PostSearchCondition postSearchCondition);

    SliceResponseDto<MyPostListDto, Post> findMyPostList(SliceRequestDto sliceRequestDto, long memberId, PostSearchCondition postSearchCondition);

    SliceResponseDto<BookmarkPostListDto, Post> findBookmarkedPostList(SliceRequestDto sliceRequestDto, long memberId, PostSearchCondition postSearchCondition);
}
