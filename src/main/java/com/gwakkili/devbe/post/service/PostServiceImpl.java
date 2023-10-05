package com.gwakkili.devbe.post.service;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.dto.PostDto;
import com.gwakkili.devbe.post.dto.PostSaveDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
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
                .build();

        postRepository.save(newPost);
    }

    @Override
    public void reportPost(Long postId, long memberId) {


    }

    @Override
    public void bookmarkPost(Long postId, long memberId) {

    }

    @Override
    public void recommendPost(Long postId, long memberId) {

    }

    @Override
    public void deletePost(Long postId, long memberId) {
        Post findPost = find(postId);
        postRepository.delete(findPost);
    }

    @Override
    public void updatePost(Long postId, long memberId) {
        Post findPost = find(postId);
//        findPost.update();
    }

    @Override
    public Post find(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
    }

    @Override
    public PostDto findPostDto(Long postId) {
        Post findPost = find(postId);
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
