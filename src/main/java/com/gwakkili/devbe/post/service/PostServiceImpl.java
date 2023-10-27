package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.event.DeleteByManagerEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.dto.request.FreePostSaveDto;
import com.gwakkili.devbe.post.dto.request.NeedHelpPostSaveDto;
import com.gwakkili.devbe.post.dto.request.PostSearchCondition;
import com.gwakkili.devbe.post.dto.request.PostUpdateDto;
import com.gwakkili.devbe.post.dto.response.*;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import com.gwakkili.devbe.post.entity.PostRecommend;
import com.gwakkili.devbe.post.repository.PostBookmarkRepository;
import com.gwakkili.devbe.post.repository.PostQueryRepository;
import com.gwakkili.devbe.post.repository.PostRecommendRepository;
import com.gwakkili.devbe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final PostRecommendRepository postRecommendRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public PostDetailDto saveNewFreePost(FreePostSaveDto postSaveDto, long memberId) {
        Member writer = memberRepository.findWithImageByMemberId(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        Post newPost = Post.builder()
                .title(postSaveDto.getTitle())
                .content(postSaveDto.getContent())
                .writer(writer)
                .boardType(Post.BoardType.FREE)
                .major(null)
                .tag(postSaveDto.getTag())
                .isAnonymous(postSaveDto.getIsAnonymous())
                .build();

        if(postSaveDto.getImageUrls() != null) newPost.addImages(postSaveDto.getImageUrls());

        Post savePost = postRepository.save(newPost);
        return PostDetailDto.of(savePost);
    }

    @Override
    @Transactional
    public PostDetailDto saveNewNeedHelpPost(NeedHelpPostSaveDto postSaveDto, long memberId) {
        Member writer = memberRepository.findWithImageByMemberId(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        Post newPost = Post.builder()
                .title(postSaveDto.getTitle())
                .content(postSaveDto.getContent())
                .writer(writer)
                .boardType(Post.BoardType.NEED_HELP)
                .major(String.valueOf(postSaveDto.getMajorCategory()))
                .tag(null)
                .isAnonymous(postSaveDto.getIsAnonymous())
                .build();

        if(postSaveDto.getImageUrls() != null) newPost.addImages(postSaveDto.getImageUrls());

        Post savePost = postRepository.save(newPost);
        return PostDetailDto.of(savePost);
    }

    @Override
    @Transactional
    public void updatePost(PostUpdateDto postUpdateDto, Long postId, long memberId) {
        Post findPost = find(postId);

        // ! 게시글을 수정하려는 사용자가 글 작성자 본인이 아닐 경우
        if (findPost.getWriter().getMemberId() != memberId){
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }

        // ! 이미지 교체 로직 쿼리 갯수 확인 필요 -> imageUrls Lazy Loading 확인
        findPost.update(postUpdateDto.getTitle(), postUpdateDto.getContent(),postUpdateDto.getBoardType(), postUpdateDto.getTag(), postUpdateDto.getMajor(), postUpdateDto.isAnonymous(),postUpdateDto.getImageUrls());
    }
    @Override
    @Transactional
    public void deletePost(Long postId, long memberId, Set<Member.Role> roles) {
        Post findPost = find(postId);

        if(roles.contains(Member.Role.ROLE_MANAGER)){
            publisher.publishEvent(new DeleteByManagerEvent(findPost.getWriter().getMemberId()));
        } else if (findPost.getWriter().getMemberId() != memberId){
            // ! 게시글을 삭제하려는 사용자가 글 작성자 본인 또는 ADMIN이 아닐 경우
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }
        postRepository.delete(findPost);
    }

    @Override
    @Transactional
    public void bookmarkPost(Long postId, long memberId) {
        Member findMember = memberRepository.getReferenceById(memberId);
        Post findPost = postRepository.getReferenceById(postId);

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
    @Transactional
    public void recommendPost(Long postId, long memberId) {
        Member findMember = memberRepository.getReferenceById(memberId);
        Post findPost = postRepository.getReferenceById(postId);

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
    public SliceResponseDto<BasicPostListDto, Post> findPostList(SliceRequestDto sliceRequestDto, PostSearchCondition postSearchCondition) {
        Pageable pageable = sliceRequestDto.getPageable();

        Slice<Post> slice = postQueryRepository.findPostList(pageable, postSearchCondition);
        Function<Post, BasicPostListDto> fn = BasicPostListDto::of;
        return new SliceResponseDto<>(slice, fn);
    }

    @Override
    public SliceResponseDto<MyPostListDto, Post> findMyPostList(SliceRequestDto sliceRequestDto, long memberId, PostSearchCondition postSearchCondition) {
        Pageable pageable = sliceRequestDto.getPageableDefaultSorting();
        Member findMember = memberRepository.getReferenceById(memberId);

        Slice<Post> slice = postRepository.findByWriterAndBoardType(pageable, findMember, postSearchCondition.getBoardType());
        Function<Post, MyPostListDto> fn = MyPostListDto::of;
        return new SliceResponseDto<>(slice, fn);
    }

    @Override
    public SliceResponseDto<BookmarkPostListDto, Post> findBookmarkedPostList(SliceRequestDto sliceRequestDto, long memberId, PostSearchCondition postSearchCondition) {
        Pageable pageable = sliceRequestDto.getPageable();
        Member findMember = memberRepository.getReferenceById(memberId);

        Slice<Post> slice = postRepository.findBookmarked(pageable, findMember, postSearchCondition.getBoardType());
        Function<Post, BookmarkPostListDto> fn = BookmarkPostListDto::of;
        return new SliceResponseDto<>(slice, fn);
    }


    @Override
    public SliceResponseDto<ReportPostListDto, Object[]> findReportedPostList(SliceRequestDto sliceRequestDto) {
        Pageable pageable = sliceRequestDto.getPageable();

        Slice<Object[]> postList= postRepository.findReported(pageable);
        Function<Object[], ReportPostListDto> fn = (object -> ReportPostListDto.of((Post) object[0],(long) object[1]));
        return new SliceResponseDto<>(postList, fn);
    }
    @Override
    public ListResponseDto<BasicPostListDto, Post> findWeeklyHotPostList() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(7);

        List<Post> postList = postRepository.findWeeklyHot(start, end);
        Function<Post, BasicPostListDto> fn = BasicPostListDto::of;
        return new ListResponseDto<>(postList, fn);
    }

    @Override
    public ListResponseDto<BasicPostListDto,Object[]> findDailyHotPostList() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(1);

        List<Object[]> postList = postRepository.findDailyHot(start, end);
        Function<Object[], BasicPostListDto> fn = (object -> BasicPostListDto.of((Long)object[0],(String) object[1],(String)object[2],((Timestamp) object[3]).toLocalDateTime()));
        return new ListResponseDto<>(postList, fn);
    }
}
