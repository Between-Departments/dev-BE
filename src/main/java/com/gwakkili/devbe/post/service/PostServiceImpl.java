package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.dto.request.PostSaveDto;
import com.gwakkili.devbe.post.dto.request.PostUpdateDto;
import com.gwakkili.devbe.post.dto.response.BookmarkPostListDto;
import com.gwakkili.devbe.post.dto.response.MyPostListDto;
import com.gwakkili.devbe.post.dto.response.PostDetailDto;
import com.gwakkili.devbe.post.dto.response.ReportPostListDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import com.gwakkili.devbe.post.entity.PostRecommend;
import com.gwakkili.devbe.post.repository.PostBookmarkRepository;
import com.gwakkili.devbe.post.repository.PostRecommendRepository;
import com.gwakkili.devbe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final PostRecommendRepository postRecommendRepository;
    private final MemberRepository memberRepository;


    @Override
    public PostDetailDto saveNewPost(PostSaveDto postSaveDto, long memberId) {
        Member writer = memberRepository.findWithImageAndMemberImageByMemberId(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        Post newPost = Post.builder()
                .title(postSaveDto.getTitle())
                .content(postSaveDto.getContent())
                .writer(writer)
                .category(postSaveDto.getCategory())
                .major(postSaveDto.getMajor())
                .isAnonymous(postSaveDto.isAnonymous())
                .build();

        Post savePost = postRepository.save(newPost);

        return PostDetailDto.of(savePost);
    }

    @Override
    @Transactional
    public void bookmarkPost(Long postId, long memberId) {
        Member findMember = memberRepository.getReferenceById(memberId);

        // ! findById VS getReferenceById
        Post findPost = find(postId);

        Optional<PostBookmark> findPostBookmark = postBookmarkRepository.findByMemberAndPost(findMember, findPost);

        findPostBookmark.ifPresentOrElse(postBookmarkRepository::delete,
                () -> {
                    PostBookmark newPostBookmark = PostBookmark.builder()
                            .post(findPost)
                            .member(findMember)
                            .build();

                    postBookmarkRepository.save(newPostBookmark);
                });
    }

    @Override
    public void recommendPost(Long postId, long memberId) {
        Member findMember = memberRepository.getReferenceById(memberId);

        // ! findById VS getReferenceById
        Post findPost = find(postId);

        Optional<PostRecommend> findPostRecommend = postRecommendRepository.findByMemberAndPost(findMember, findPost);

        findPostRecommend.ifPresentOrElse(postRecommendRepository::delete,
                () -> {
                    PostRecommend newPostRecommend = PostRecommend.builder()
                            .post(findPost)
                            .member(findMember)
                            .build();

                    postRecommendRepository.save(newPostRecommend);
                });
    }

    @Override
    public void deletePost(Long postId, long memberId, Set<Member.Role> roles) {
        Member findMember = memberRepository.getReferenceById(memberId);
        Post findPost = find(postId);

        if (findPost.getWriter().getMemberId() == findMember.getMemberId()
                || roles.contains(Member.Role.ROLE_MANAGER)){
            postRepository.delete(findPost);
        } else{
            // ! 게시글을 삭제하려는 사용자가 글 작성자 본인 또는 ADMIN이 아닐 경우
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }
    }

    @Override
    public void updatePost(PostUpdateDto postUpdateDto, Long postId, long memberId) {
        Member findMember = memberRepository.getReferenceById(memberId);
        Post findPost = find(postId);

        if (findPost.getWriter().getMemberId() == findMember.getMemberId()){
            findPost.update(postUpdateDto.getTitle(), postUpdateDto.getContent(), postUpdateDto.getCategory(), postUpdateDto.getMajor(), postUpdateDto.isAnonymous());
        } else{
            // ! 게시글을 수정하려는 사용자가 글 작성자 본인이 아닐 경우
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }
    }

    private Post find(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
    }

    @Override
    public PostDetailDto findPostDto(Long postId) {
        Post findPost = postRepository.findWithDetailByPostId(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
        return PostDetailDto.of(findPost);
    }


    @Override
    public SliceResponseDto<PostDetailDto, Post> findPostList(SliceRequestDto sliceRequestDto) {
//        String keyword = sliceRequestDto.getKeyword();
        // TODO 목록을 조회할때 필요한 QueryParameter들 + 정렬 기준에 대한 정확한 명세가 필요함. 그 전까지 보류

        Pageable pageable = sliceRequestDto.getPageable();

        Slice<Post> slice = postRepository.findSliceBy(pageable);
        Function<Post, PostDetailDto> fn = PostDetailDto::of;
        return new SliceResponseDto<>(slice, fn);
    }

    @Override
    public SliceResponseDto<MyPostListDto, Post> findMyPostList(SliceRequestDto sliceRequestDto, long memberId) {
        Pageable pageable = sliceRequestDto.getPageableDefaultSorting();
        Member findMember = memberRepository.getReferenceById(memberId);

        Slice<Post> slice = postRepository.findByWriter(findMember, pageable);
        Function<Post, MyPostListDto> fn = MyPostListDto::of;
        return new SliceResponseDto<>(slice, fn);
    }

    @Override
    public SliceResponseDto<BookmarkPostListDto, PostBookmark> findBookmarkedPostList(SliceRequestDto sliceRequestDto, long memberId) {
        Pageable pageable = sliceRequestDto.getPageable();
        Member findMember = memberRepository.getReferenceById(memberId);

        Slice<PostBookmark> slice = postBookmarkRepository.findByMember(findMember, pageable);
        Function<PostBookmark, BookmarkPostListDto> fn = BookmarkPostListDto::of;
        return new SliceResponseDto<>(slice, fn);
    }
    @Override
    public SliceResponseDto<ReportPostListDto, Object[]> findReportedPostList(SliceRequestDto sliceRequestDto) {
        Pageable pageable = sliceRequestDto.getPageable();

        Slice<Object[]> postList= postRepository.findReported(pageable);
        Function<Object[], ReportPostListDto> fn = (object -> ReportPostListDto.of((Post) object[0],(long) object[1]));
        return new SliceResponseDto(postList, fn);
    }
}
