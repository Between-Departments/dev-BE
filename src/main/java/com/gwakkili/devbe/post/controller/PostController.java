package com.gwakkili.devbe.post.controller;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.post.dto.request.FreePostSaveDto;
import com.gwakkili.devbe.post.dto.request.NeedHelpPostSaveDto;
import com.gwakkili.devbe.post.dto.request.PostSearchCondition;
import com.gwakkili.devbe.post.dto.request.PostUpdateDto;
import com.gwakkili.devbe.post.dto.response.*;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.service.PostService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Tag(name = "게시글", description = "게시글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;

    @Operation(method = "GET", summary = "주간 인기 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주간 인기 게시글 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/weeklyhot")
    public ListResponseDto<BasicPostListDto, Post> getWeeklyHotPostList(){
        return postService.findWeeklyHotPostList();
    }

    @Operation(method = "GET", summary = "금일 계열별 인기 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "금일 계열별 인기 게시글 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/dailyhot")
    public ListResponseDto<BasicPostListDto, Object[]> getDailyHotNeedHelpPostList(){
        return postService.findDailyHotPostList();
    }


    @Operation(method = "GET", summary = "게시글 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDetailDto.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{postId}")
    public PostDetailDto getPost(@PathVariable Long postId,
                                 @AuthenticationPrincipal MemberDetails memberDetails,
                                 HttpServletRequest req, HttpServletResponse res){

        boolean doViewCountUp = viewCountUp(postId, req, res);
        return postService.findPostDto(postId, memberDetails != null ? memberDetails.getMemberId() : null, doViewCountUp);
    }

    private boolean viewCountUp(Long postId, HttpServletRequest req, HttpServletResponse res) {
        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("postView")){
                    oldCookie = cookie;
                }
            }
        }

        // ! 조회 정보를 담은 쿠키에 해당 게시물의 아이디가 없는 경우
        if (oldCookie != null){
            if(!oldCookie.getValue().contains("["+ postId.toString() +"]")){
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId + "]");
                oldCookie.setMaxAge(Long.valueOf(getExpiration().getSeconds()).intValue());
                res.addCookie(oldCookie);
                return true;
            } else{
                return false;
            }
        } else{
            ResponseCookie newCookie = ResponseCookie.from("postView","[" + postId + "]")
                    .path("/")
                    .maxAge(getExpiration())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .build();

            res.addHeader("Set-Cookie",newCookie.toString());
            return true;
        }
    }

    private Duration getExpiration(){
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.with(LocalTime.MAX);
        return Duration.between(start, end);
    }

    @Operation(method = "GET", summary = "게시글 목록 조회 (조회 조건 적용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping
    public SliceResponseDto<BasicPostListDto, Post> getPostList(@ParameterObject SliceRequestDto sliceRequestDto, @ParameterObject PostSearchCondition postSearchCondition){
        return postService.findPostList(sliceRequestDto, postSearchCondition);
    }

    @Operation(method = "GET", summary = "신고된 게시글 목록 조회 (ADMIN 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고된 게시글 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/report")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public SliceResponseDto<ReportPostListDto, Object[]> getReportedPostList(@ParameterObject SliceRequestDto sliceRequestDto){
        return postService.findReportedPostList(sliceRequestDto);
    }

    @Operation(method = "GET", summary = "내가 작성한 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 작성한 게시글 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public SliceResponseDto<MyPostListDto, Post> getMyPostList(@ParameterObject SliceRequestDto sliceRequestDto, @ParameterObject PostSearchCondition postSearchCondition, @AuthenticationPrincipal MemberDetails memberDetails){
        return postService.findMyPostList(sliceRequestDto, memberDetails.getMemberId(), postSearchCondition);
    }

    @Operation(method = "GET", summary = "북마크한 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크한 게시글 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/bookmark")
    @PreAuthorize("isAuthenticated()")
    public SliceResponseDto<BookmarkPostListDto, Post> getBookmarkedPostList(@ParameterObject SliceRequestDto sliceRequestDto, @ParameterObject PostSearchCondition postSearchCondition, @AuthenticationPrincipal MemberDetails memberDetails){
        return postService.findBookmarkedPostList(sliceRequestDto, memberDetails.getMemberId(), postSearchCondition);
    }

    @Operation(method = "POST", summary = "자유게시판 글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDetailDto.class)))
    })
    @PostMapping("/free")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDetailDto create(@RequestBody @Valid FreePostSaveDto postSaveDto,
                                @AuthenticationPrincipal MemberDetails memberDetails){
        return postService.saveNewFreePost(postSaveDto,memberDetails.getMemberId());
    }

    @Operation(method = "POST", summary = "도움이 필요해요 글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDetailDto.class)))
    })
    @PostMapping("/needhelp")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDetailDto create(@RequestBody @Valid NeedHelpPostSaveDto postSaveDto,
                                @AuthenticationPrincipal MemberDetails memberDetails){
        return postService.saveNewNeedHelpPost(postSaveDto,memberDetails.getMemberId());
    }

    @Operation(method = "PATCH", summary = "특정 게시글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 게시글 수정 성공")
    })
    @PatchMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody @Valid PostUpdateDto postUpdateDto,
                       @PathVariable Long postId,
                       @AuthenticationPrincipal MemberDetails memberDetails) {
        postService.updatePost(postUpdateDto, postId, memberDetails.getMemberId());
    }

    @Operation(method = "DELETE", summary = "특정 게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 게시글 삭제 성공")
    })
    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long postId, @AuthenticationPrincipal MemberDetails memberDetails){
        postService.deletePost(postId,memberDetails.getMemberId(), memberDetails.getRoles());
    }

    @Operation(method = "POST", summary = "특정 게시글 북마크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 게시글 북마크 성공")
    })
    @PatchMapping("/{postId}/bookmark")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void bookmark(@PathVariable Long postId, @AuthenticationPrincipal MemberDetails memberDetails){
        postService.bookmarkPost(postId,memberDetails.getMemberId());
    }

    @Operation(method = "POST", summary = "특정 게시글 추천")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 게시글 추천 성공")
    })
    @PatchMapping("/{postId}/recommend")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void recommend(@PathVariable Long postId, @AuthenticationPrincipal MemberDetails memberDetails){
        postService.recommendPost(postId,memberDetails.getMemberId());
    }

}
