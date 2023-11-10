package com.gwakkili.devbe.post;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시물 조회 테스트")
public class PostGetTests extends DevBeApplicationTests {

    @Nested
    @DisplayName("단건 게시물 조회")
    class SinglePostGetTests {
        String url1 = "/api/posts/{postId}";

        @DisplayName("익명 사용자 성공")
        @Test
        public void successByAnonymous() throws Exception {
            Long postId = 2L;

            mockMvc.perform(get(url1,postId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("postId").value(postId))
                    .andExpect(jsonPath("title").exists())
                    .andExpect(jsonPath("content").exists())
                    .andExpect(jsonPath("boardType").value(Post.BoardType.NEED_HELP.name()))
                    .andExpect(jsonPath("majorCategory").exists())
                    .andExpect(jsonPath("isAnonymous").exists())
                    .andExpect(jsonPath("writer.memberId").exists())
                    .andExpect(jsonPath("writer.nickname").exists())
                    .andExpect(jsonPath("writer.school").exists())
                    .andExpect(jsonPath("writer.major").exists())
                    .andExpect(jsonPath("writer.imageUrl").exists())
                    .andExpect(jsonPath("recommendCount").exists())
                    .andExpect(jsonPath("replyCount").exists())
                    .andExpect(jsonPath("images").exists())
                    .andExpect(jsonPath("isAnonymous").exists())
                    .andExpect(jsonPath("isMine").value(Boolean.FALSE))
                    .andExpect(jsonPath("isBookmarked").value(Boolean.FALSE))
                    .andExpect(jsonPath("isRecommended").value(Boolean.FALSE))
                    .andDo(print());
        }

        @DisplayName("작성자 본인 성공")
        @Test
        @WithMockMember
        public void successOneByOwner() throws Exception {
            Long postId = 2L;

            mockMvc.perform(get(url1,postId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("postId").value(postId))
                    .andExpect(jsonPath("title").exists())
                    .andExpect(jsonPath("content").exists())
                    .andExpect(jsonPath("boardType").value(Post.BoardType.NEED_HELP.name()))
                    .andExpect(jsonPath("majorCategory").exists())
                    .andExpect(jsonPath("isAnonymous").exists())
                    .andExpect(jsonPath("writer.memberId").exists())
                    .andExpect(jsonPath("writer.nickname").exists())
                    .andExpect(jsonPath("writer.school").exists())
                    .andExpect(jsonPath("writer.major").exists())
                    .andExpect(jsonPath("writer.imageUrl").exists())
                    .andExpect(jsonPath("recommendCount").exists())
                    .andExpect(jsonPath("replyCount").exists())
                    .andExpect(jsonPath("images").exists())
                    .andExpect(jsonPath("isAnonymous").exists())
                    .andExpect(jsonPath("isMine").value(Boolean.TRUE))
                    .andExpect(jsonPath("isBookmarked").value(Boolean.TRUE))
                    .andExpect(jsonPath("isRecommended").value(Boolean.TRUE))
                    .andDo(print());
        }

        @DisplayName("실패 : 게시물이 존재하지 않음")
        @Test
        public void failOne() throws Exception {
            Long postId = 10000L;

            mockMvc.perform(get(url1,postId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_POST.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.NOT_FOUND_POST.getMessage()))
                    .andDo(print());

        }
    }

    @Nested
    @DisplayName("게시물 목록 조회")
    class PostListGetTests {
        String url2 = "/api/posts";

        @DisplayName("성공 : 검색 조건 아무것도 없음")
        @Test
        public void noSearchCondition() throws Exception {
            mockMvc.perform(get(url2))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(10)))
                    .andExpect(jsonPath("dataList[*].postId").exists())
                    .andDo(print());
        }

        @DisplayName("성공 : 검색 조건 적용")
        @Test
        public void searchCondition() throws Exception {
            mockMvc.perform(get(url2)
//                            .param("page")
                            .param("size","5")
//                            .param("sortBy")
//                            .param("direction")
//                            .param("keyword")
                            .param("boardType",Post.BoardType.FREE.name())
//                            .param("majorCategory")
//                            .param("tag"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(5)))
                    .andExpect(jsonPath("dataList[*].boardType").value(everyItem(equalTo(Post.BoardType.FREE.name()))))
                    .andDo(print());
        }

        @DisplayName("데이터 없음 : 검색조건에 맞는 데이터가 없는 경우")
        @Test
        public void noResult() throws Exception {
            mockMvc.perform(get(url2)
//                            .param("page")
                                    .param("size","5")
//                            .param("sortBy")
//                            .param("direction")
                            .param("keyword","존재하지 않는 키워드")
                            .param("boardType",Post.BoardType.FREE.name())
//                            .param("majorCategory")
//                            .param("tag"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList").isEmpty())
                    .andDo(print());
        }

        @DisplayName("데이터 없음 : 총 데이터 갯수를 벗어난 페이지를 요청한 경우")
        @Test
        public void outOfTotalPage() throws Exception {
            mockMvc.perform(get(url2)
                                    .param("page","200")
                                    .param("size","10")
//                            .param("sortBy")
//                            .param("direction")
//                            .param("keyword")
//                            .param("boardType",Post.BoardType.FREE.name())
//                            .param("majorCategory")
//                            .param("tag"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList").isEmpty())
                    .andDo(print());
        }

    }


    @Nested
    @DisplayName("인기 게시물 조회")
    class HotPostGetTests {
        String weekly = "/api/posts/weeklyhot";
        String daily = "/api/posts/dailyhot";

        @DisplayName("성공 : 주간 인기 게시물")
        @Test
        public void successWeekly() throws Exception {
            mockMvc.perform(get(weekly))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(5)))
                    .andDo(print());


        }

//        @DisplayName("데이터 없음 : ")
//        @Test
//        public void noResultWeekly() throws Exception {
//            mockMvc.perform(get(weekly))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("dataList").exists())
//                    .andExpect(jsonPath("dataList").isArray())
//                    .andExpect(jsonPath("dataList").isEmpty())
//                    .andDo(print());
//        }


        @DisplayName("성공 : 최근 24시간 인기 게시물")
        @Test
        public void successDaily() throws Exception {
            mockMvc.perform(get(daily))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(6)))
                    .andDo(print());

        }

//        @DisplayName("데이터 없음 : ")
//        @Test
//        public void noResultDaily() throws Exception {
//            mockMvc.perform(get(daily))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("dataList").exists())
//                    .andExpect(jsonPath("dataList").isArray())
//                    .andExpect(jsonPath("dataList").isEmpty())
//                    .andDo(print());
//        }
    }

    @Nested
    @DisplayName("나의 게시물 목록 조회")
    class MyPostGetTests {
        String url = "/api/posts/my";

        @DisplayName("성공")
        @Test
        @WithMockMember
        public void success() throws Exception {
            mockMvc.perform(get(url)
                            .param("boardType", Post.BoardType.FREE.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(1)))
                    .andExpect(jsonPath("dataList[*].writerId").value(everyItem(equalTo(1))))
                    .andDo(print());

        }

        @Test
        @DisplayName("실패: 인증되지 않은 사용자")
        public void failByUnauthorized() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("code").value(ExceptionCode.UNAUTHORIZED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.UNAUTHORIZED.getMessage()))
                    .andDo(print());
        }

        @DisplayName("데이터 없음 : 작성한 글이 하나도 없는 경우")
        @Test
        @WithMockMember(memberId = 101)
        public void noResult() throws Exception {

            mockMvc.perform(get(url)
                            .param("boardType", Post.BoardType.FREE.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList").isEmpty())
                    .andDo(print());

        }
    }

    @Nested
    @DisplayName("신고된 게시물 목록 조회(ADMIN 전용)")
    class ReportedPostGetTests {
        String url = "/api/posts/report";

        @Test
        @DisplayName("성공")
        @WithMockMember(roles = "ROLE_MANAGER")
        public void success() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(10)))
                    .andExpect(jsonPath("dataList[*].reportCount").value(everyItem(greaterThan(0))))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 권한없음")
        @WithMockMember(roles = "ROLE_USER")
        public void failByAccessDenied() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("북마크한 게시물 목록 조회")
    class BookmarkedPostGetTests {
        String url = "/api/posts/bookmark";

        @DisplayName("성공")
        @Test
        @WithMockMember
        public void success() throws Exception {
            mockMvc.perform(get(url)
                            .param("boardType", Post.BoardType.FREE.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(10)))
                    .andDo(print());

        }

        @Test
        @DisplayName("실패: 인증되지 않은 사용자")
        public void failByUnauthorized() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("code").value(ExceptionCode.UNAUTHORIZED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.UNAUTHORIZED.getMessage()))
                    .andDo(print());
        }

        @DisplayName("데이터 없음 : 북마크한 글이 하나도 없는 경우")
        @Test
        @WithMockMember(memberId = 101)
        public void noResult() throws Exception {

            mockMvc.perform(get(url)
                            .param("boardType", Post.BoardType.FREE.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList").isEmpty())
                    .andDo(print());

        }
    }

}
