package com.gwakkili.devbe.post.controller;

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
import com.gwakkili.devbe.post.service.PostService;
import com.gwakkili.devbe.report.entity.PostReport;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "게시글", description = "게시글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;

    @Operation(method = "GET", summary = "게시글 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDetailDto.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{postId}")
    public PostDetailDto getPost(@PathVariable Long postId){
        return postService.findPostDto(postId);
    }

    @GetMapping
    public SliceResponseDto<PostDetailDto, Post> getPostList(@ParameterObject SliceRequestDto sliceRequestDto){
        return postService.findPostList(sliceRequestDto);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public SliceResponseDto<PostDetailDto, PostReport> getReportedPostList(@ParameterObject SliceRequestDto sliceRequestDto){
        return postService.findReportedPostList(sliceRequestDto);
    }

    @GetMapping("/my")
    public SliceResponseDto<MyPostListDto, Post> getMyPostList(@ParameterObject SliceRequestDto sliceRequestDto, @AuthenticationPrincipal MemberDetails memberDetails){
        return postService.findMyPostList(sliceRequestDto, memberDetails.getMemberId());
    }

    @GetMapping("/bookmark")
    public SliceResponseDto<BookmarkPostListDto, PostBookmark> getBookmarkedPostList(@ParameterObject SliceRequestDto sliceRequestDto, @AuthenticationPrincipal MemberDetails memberDetails){
        return postService.findBookmarkedPostList(sliceRequestDto, memberDetails.getMemberId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody PostSaveDto postSaveDto, @AuthenticationPrincipal MemberDetails memberDetails){
        postService.saveNewPost(postSaveDto,memberDetails.getMemberId());
    }

    @PostMapping("/{postId}/report")
    @ResponseStatus(HttpStatus.OK)
    public void report(@RequestBody PostReportDto postReportDto, @PathVariable Long postId, @AuthenticationPrincipal MemberDetails memberDetails){
        postService.reportPost(postReportDto, postId, memberDetails.getMemberId());
    }

    @PostMapping("/{postId}/bookmark")
    @ResponseStatus(HttpStatus.OK)
    public void bookmark(@PathVariable Long postId,@AuthenticationPrincipal MemberDetails memberDetails){
        postService.bookmarkPost(postId,memberDetails.getMemberId());
    }

    @PostMapping("/{postId}/recommend")
    @ResponseStatus(HttpStatus.OK)
    public void recommend(@PathVariable Long postId,@AuthenticationPrincipal MemberDetails memberDetails){
        postService.recommendPost(postId,memberDetails.getMemberId());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long postId,@AuthenticationPrincipal MemberDetails memberDetails){
        postService.deletePost(postId,memberDetails.getMemberId());
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody PostUpdateDto postUpdateDto, @PathVariable Long postId, @AuthenticationPrincipal MemberDetails memberDetails){
        postService.updatePost(postUpdateDto, postId, memberDetails.getMemberId());
    }


}
