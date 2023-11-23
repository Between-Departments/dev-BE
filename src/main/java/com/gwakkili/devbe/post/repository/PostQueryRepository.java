package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.dto.request.PostSearchCondition;
import com.gwakkili.devbe.post.entity.Post;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gwakkili.devbe.image.entity.QMemberImage.memberImage;
import static com.gwakkili.devbe.member.entity.QMember.member;
import static com.gwakkili.devbe.post.entity.QPost.post;
import static com.gwakkili.devbe.post.entity.QPostRecommend.postRecommend;
import static com.gwakkili.devbe.reply.entity.QReply.reply;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class PostQueryRepository {

    private final JPAQueryFactory query;

    public PostQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    // ! 특정 목록
    // * 검색 키워드, 게시판 타입, 태그, 전공
    public Slice<Post> findPostList(Pageable pageable, PostSearchCondition postSearchCondition){
        List<Tuple> result = query.
                select(post,
                        JPAExpressions.select(postRecommend.count())
                                .from(postRecommend)
                                .where(postRecommend.post.postId.eq(post.postId)),
                        JPAExpressions.select(reply.count())
                                .from(reply)
                                .where(reply.post.postId.eq(post.postId)))
                .from(post)
                .join(post.writer, member).fetchJoin()
                .join(member.image, memberImage).fetchJoin()
                .where(boardTypeEq(postSearchCondition.getBoardType()),
                        majorCategoryEq(postSearchCondition.getMajorCategory()),
                        tagEq(postSearchCondition.getTag()),
                        keywordContains(postSearchCondition.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        List<Post> content = result.stream().map(tuple -> {
            Post findPost = tuple.get(post);
            findPost.setRecommendCount(tuple.get(1,Long.class));
            findPost.setReplyCount(tuple.get(2, Long.class));
            return findPost;
        }).collect(Collectors.toList());

        // ! PostImage가 BatchSize에 의해 런타임에 제대로 갖고와지는지 확인 필요.

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }


    private BooleanExpression boardTypeEq(Post.BoardType boardType) {
        return boardType == null ? null : post.boardType.eq(boardType);
    }

    private BooleanExpression majorCategoryEq(Major.Category majorCategory) {
        return majorCategory == null ? null : post.majorCategory.eq(majorCategory);
    }

    private BooleanExpression tagEq(Post.Tag tag) {
        return tag == null ? null : post.tag.eq(tag);
    }

    private BooleanExpression keywordContains(String keyword) {
        return !hasText(keyword) ? null : post.content.contains(keyword).or(post.title.contains(keyword));
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Post.class, "post");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

}
