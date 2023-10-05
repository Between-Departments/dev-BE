package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.dto.PostDto;
import com.gwakkili.devbe.post.dto.PostReportDto;
import com.gwakkili.devbe.post.dto.PostSaveDto;
import com.gwakkili.devbe.post.dto.PostUpdateDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import com.gwakkili.devbe.post.entity.PostRecommend;
import com.gwakkili.devbe.post.repository.PostBookmarkRepository;
import com.gwakkili.devbe.post.repository.PostRecommendRepository;
import com.gwakkili.devbe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final PostRecommendRepository postRecommendRepository;
    private final MemberRepository memberRepository;


    @Override
    public void saveNewPost(PostSaveDto postSaveDto, long memberId) {
        Member writer = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        Post newPost = Post.builder()
                .title(postSaveDto.getTitle())
                .content(postSaveDto.getContent())
                .writer(writer)
                .category(postSaveDto.getCategory())
                .major(postSaveDto.getMajor())
                .isAnonymous(postSaveDto.isAnonymous())
                .build();

        postRepository.save(newPost);
    }

    @Override
    public void reportPost(PostReportDto postReportDto , Long postId, long memberId) {
        // todo 신고와 관련된 구체적인 사항이 결정되면 개발 진행

        Member reporter = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        Post findPost = find(postId);

    }

    @Override
    public void bookmarkPost(Long postId, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        Post findPost = find(postId);

        Optional<PostBookmark> findPostBookmark = postBookmarkRepository.findByMemberAndPost(findMember, findPost);

        findPostBookmark.ifPresentOrElse(postBookmarkRepository::delete,
                () -> {
                    PostBookmark newPostBookmark = PostBookmark.builder()
                            .post(findPost)
                            .member(findMember)
                            .isBookmarked(true)
                            .build();

                    postBookmarkRepository.save(newPostBookmark);
                });
    }

    @Override
    public void recommendPost(Long postId, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
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
    public void deletePost(Long postId, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        Post findPost = find(postId);

        if (findPost.getWriter().getMemberId() == findMember.getMemberId()
                || findMember.getRoles().contains(Member.Role.ROLE_MANAGER)){
            postRepository.delete(findPost);
        } else{
            // ! 게시글을 삭제하려는 사용자가 글 작성자 본인 또는 ADMIN이 아닐 경우
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }
    }

    @Override
    public void updatePost(PostUpdateDto postUpdateDto, Long postId, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        Post findPost = find(postId);

        if (findPost.getWriter().getMemberId() == findMember.getMemberId()){
            findPost.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
        } else{
            // ! 게시글을 수정하려는 사용자가 글 작성자 본인이 아닐 경우
            throw new CustomException(ExceptionCode.ACCESS_DENIED);
        }
    }

    @Override
    public Post find(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
    }

    @Override
    public PostDto findPostDto(Long postId) {
        Post findPost = postRepository.findWithWriterById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
        return PostDto.of(findPost);
    }

    @Override
    public void findReportedPostList() {

    }

    @Override
    public void findPostList() {

    }

    @Override
    public void findMyPostList(long memberId) {

    }

    @Override
    public void findBookmarkedPostList(long memberId) {

    }
}
