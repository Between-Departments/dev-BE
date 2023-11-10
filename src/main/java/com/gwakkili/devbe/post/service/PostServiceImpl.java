package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.event.DeleteByManagerEvent;
import com.gwakkili.devbe.event.DeletePostImageEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.image.repository.PostImageRepository;
import com.gwakkili.devbe.major.entity.Major;
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
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final PostRecommendRepository postRecommendRepository;
    private final PostImageRepository postImageRepository;
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
                .majorCategory(null)
                .tag(postSaveDto.getTag())
                .isAnonymous(postSaveDto.getIsAnonymous())
                .build();

        if(postSaveDto.getImageUrls() != null) newPost.addImages(postSaveDto.getImageUrls());

        Post savePost = postRepository.save(newPost);
        return PostDetailDto.of(savePost,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);
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
                .majorCategory(postSaveDto.getMajorCategory())
                .tag(null)
                .isAnonymous(postSaveDto.getIsAnonymous())
                .build();

        if(postSaveDto.getImageUrls() != null) newPost.addImages(postSaveDto.getImageUrls());

        Post savePost = postRepository.save(newPost);
        return PostDetailDto.of(savePost,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);
    }

    @Override
    @Transactional
    public void updatePost(PostUpdateDto postUpdateDto, Long postId, long memberId) {
        Post findPost = postRepository.findWithImagesByPostId(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));

        // ! 게시글을 수정하려는 사용자가 글 작성자 본인이 아닐 경우
        if (findPost.getWriter().getMemberId() != memberId){
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }

        List<PostImage> images = findPost.getImages();
        if (!images.isEmpty()) postImageRepository.deleteAllInBatch(images);

        findPost.update(postUpdateDto.getTitle(), postUpdateDto.getContent(), postUpdateDto.getMajorCategory(), postUpdateDto.getTag(), postUpdateDto.getIsAnonymous(),postUpdateDto.getImageUrls());
    }

    @Override
    @Transactional
    public void deletePost(Long postId, long memberId, Set<Member.Role> roles) {
        Post findPost = postRepository.findWithImagesByPostId(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));

        if (roles.contains(Member.Role.ROLE_MANAGER)) {
            publisher.publishEvent(new DeleteByManagerEvent(findPost.getWriter().getMemberId()));
        } else if (findPost.getWriter().getMemberId() != memberId) {
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }

        List<PostImage> images = findPost.getImages();

        if(!images.isEmpty()){
            List<String> imageUrls = images.stream()
                    .map(PostImage::getUrl).collect(Collectors.toList());
            publisher.publishEvent(new DeletePostImageEvent(imageUrls));
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

    @Override
    @Transactional
    public PostDetailDto findPostDto(Long postId, Long memberId, boolean doCountUp) {
        Post findPost = postRepository.findWithDetailByPostId(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));

        if(doCountUp) findPost.addViewCount();
        PostDetailDto postDetailDto = PostDetailDto.of(findPost);

        Optional<Member> findMember = Optional.ofNullable(memberId).map(memberRepository::getReferenceById);

        findMember.ifPresentOrElse(member -> {
            postDetailDto.setRecommended(postRecommendRepository.existsByMemberAndPost(member, findPost));
            postDetailDto.setBookmarked(postBookmarkRepository.existsByMemberAndPost(member,findPost));
            postDetailDto.setMine(member.getMemberId() == findPost.getWriter().getMemberId());
        },() -> {
            postDetailDto.setRecommended(Boolean.FALSE);
            postDetailDto.setBookmarked(Boolean.FALSE);
            postDetailDto.setMine(Boolean.FALSE);
        });

        return postDetailDto;
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
        Pageable pageable = sliceRequestDto.getPageable();
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
        Function<Object[], BasicPostListDto> fn = (object -> BasicPostListDto.of((Long)object[0],(String) object[1],Major.Category.valueOf((String)object[2]),((Timestamp) object[3]).toLocalDateTime()));
        return new ListResponseDto<>(postList, fn);
    }
}
