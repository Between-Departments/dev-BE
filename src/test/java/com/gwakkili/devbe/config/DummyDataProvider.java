package com.gwakkili.devbe.config;

import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.chat.repository.ChatMessageRepository;
import com.gwakkili.devbe.chat.repository.ChatRoomRepository;
import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.major.repository.MajorRepository;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import com.gwakkili.devbe.post.entity.PostRecommend;
import com.gwakkili.devbe.post.repository.PostBookmarkRepository;
import com.gwakkili.devbe.post.repository.PostRecommendRepository;
import com.gwakkili.devbe.post.repository.PostRepository;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.entity.ReplyRecommend;
import com.gwakkili.devbe.reply.repository.ReplyRecommendRepository;
import com.gwakkili.devbe.reply.repository.ReplyRepository;
import com.gwakkili.devbe.report.entity.PostReport;
import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.report.entity.Report;
import com.gwakkili.devbe.report.repository.PostReportRepository;
import com.gwakkili.devbe.report.repository.ReplyReportRepository;
import com.gwakkili.devbe.school.entity.School;
import com.gwakkili.devbe.school.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@TestComponent
public class DummyDataProvider implements ApplicationRunner {

    private SchoolRepository schoolRepository;

    private MajorRepository majorRepository;

    private MemberRepository memberRepository;

    private PostRepository postRepository;

    private ReplyRepository replyRepository;

    private ReplyReportRepository replyReportRepository;

    private PostReportRepository postReportRepository;

    private PostBookmarkRepository postBookmarkRepository;

    private PostRecommendRepository postRecommendRepository;

    private ReplyRecommendRepository replyRecommendRepository;

    private ChatRoomRepository chatRoomRepository;

    private ChatMessageRepository chatMessageRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DummyDataProvider() {
    }

    @Autowired
    public DummyDataProvider(SchoolRepository schoolRepository,
                             MajorRepository majorRepository,
                             MemberRepository memberRepository,
                             PostRepository postRepository,
                             ReplyRepository replyRepository,
                             ReplyReportRepository replyReportRepository,
                             PostReportRepository postReportRepository,
                             PostBookmarkRepository postBookmarkRepository,
                             PostRecommendRepository postRecommendRepository,
                             ReplyRecommendRepository replyRecommendRepository,
                             ChatRoomRepository chatRoomRepository,
                             ChatMessageRepository chatMessageRepository) {
        this.schoolRepository = schoolRepository;
        this.majorRepository = majorRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.replyReportRepository = replyReportRepository;
        this.postReportRepository = postReportRepository;
        this.postBookmarkRepository = postBookmarkRepository;
        this.postRecommendRepository = postRecommendRepository;
        this.replyRecommendRepository = replyRecommendRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }


    // * 테스트용 대학교 -> 총 10개
    private void saveSchool(){
        List<School> schools = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i-> {
            School school = School.builder()
                    .mail("test" + i + ".ac.kr")
                    .name("테스트대학" + i)
                    .build();
            schools.add(school);
        });
        schoolRepository.saveAll(schools);
    }

    // * 테스트용 계열 + 학과 -> 계열 총 5개, 학과 총 30개, 계열당 학과 갯수는 랜덤함
    private void saveMajor(){
        List<Major> majors = new ArrayList<>();
        IntStream.rangeClosed(1, 30).forEach(i->{
            Major major = Major.builder().
                    category(Major.Category.values()[new Random().nextInt(Major.Category.values().length)])
                    .name("테스트학과" + i)
                    .build();
            majors.add(major);
        });
        majorRepository.saveAll(majors);
    }


    // * 테스트용 사용자 -> 총 101명 (관리자 1명, 일반 사용자 100명)
    private void saveMember(){
        Member member = Member.builder()
                .mail("test@test1.ac.kr")
                .nickname("테스트멤버")
                .password(passwordEncoder.encode("a12341234!"))
                .major("테스트학과1")
                .school("테스트대학1")
                .build();
        member.addRole(Member.Role.ROLE_MANAGER);
        member.setImage(new MemberImage("http://test.com/images/memberImage.jpg"));
        memberRepository.save(member);

        List<Member> members = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i->{
            Member member2 = Member.builder()
                    .mail("test"+i+"@test" + new Random().nextInt(1, 10) + ".ac.kr")
                    .nickname("테스트멤버" + i)
                    .password(passwordEncoder.encode("a12341234!"))
                    .major("테스트학과" + new Random().nextInt(1, 30))
                    .school("테스트대학" + new Random().nextInt(1, 10))
                    .build();
            member2.addRole(Member.Role.ROLE_USER);
            member2.setImage(new MemberImage("http://test.com/images/memberImage" + i + ".jpg"));
            members.add(member2);
        });
        memberRepository.saveAll(members);
    }

    // * 테스트용 게시물 -> 총 200개 (총 100명의 사용자별 2개씩, 도움이 필요해요 1 + 자유게시판 1)
    // * 테스트용 익명 게시물 -> 총 100개 (총 100명의 사용자별 1개씩, 도움이 필요해요 or 자유게시판 1, 이미지 X)
    // * 테스트용 게시물 이미지 -> 총 600개 (총 200개의 게시물별 3개씩)
    private void savePost() {
        List<Post> posts = new ArrayList<>();

        IntStream.rangeClosed(1,100).forEach(i ->{
            Post freePost = Post.builder()
                    .title("FreePostTitle" + i)
                    .content("FreePostContent" + i)
                    .boardType(Post.BoardType.FREE)
                    .tag(Post.Tag.values()[new Random().nextInt(Post.Tag.values().length)])
                    .writer(memberRepository.getReferenceById((long) i))
                    .isAnonymous(false)
                    .build();

            List<String> imageUrls = new ArrayList<>();

            for(int j=1; j<4; j++){
                imageUrls.add("http://test.com/images/postImage" + j + "_FreePost" + i +".jpg");
            }
            freePost.addImages(imageUrls);

            posts.add(freePost);

            Post needHelpPost = Post.builder()
                    .title("NeedHelpPostTitle"+ i)
                    .content("NeedHelpPostContent"+i)
                    .boardType(Post.BoardType.NEED_HELP)
                    .major(String.valueOf(Major.Category.values()[new Random().nextInt(Major.Category.values().length)]))
                    .writer(memberRepository.getReferenceById((long) i+1))
                    .isAnonymous(false)
                    .build();

            List<String> imageUrls1 = new ArrayList<>();

            for(int j=1; j<4; j++){
                imageUrls1.add("http://test.com/images/postImage" + j + "_NeedHelpPost" + i +".jpg");
            }
            needHelpPost.addImages(imageUrls1);

            posts.add(needHelpPost);

            if (i%2== 0) {

                Post anonymoustFreePost = Post.builder()
                        .title("FreePostTitle" + i)
                        .content("FreePostContent" + i)
                        .boardType(Post.BoardType.FREE)
                        .tag(Post.Tag.values()[new Random().nextInt(Post.Tag.values().length)])
                        .writer(memberRepository.getReferenceById((long) i + 1))
                        .isAnonymous(true)
                        .build();

                posts.add(anonymoustFreePost);

            } else{
                Post anonymoustNeedHelpPost = Post.builder()
                        .title("NeedHelpPostTitle"+ i)
                        .content("NeedHelpPostContent"+i)
                        .boardType(Post.BoardType.NEED_HELP)
                        .major(String.valueOf(Major.Category.values()[new Random().nextInt(Major.Category.values().length)]))
                        .writer(memberRepository.getReferenceById((long) i+1))
                        .isAnonymous(true)
                        .build();

                posts.add(anonymoustNeedHelpPost);
            }


        });

        postRepository.saveAll(posts);
    }

    // * 테스트용 댓글 -> 총 400개 (댓글단 게시물, 댓글 작성자 랜덤)
    // * 테스트용 익명댓글 -> 총 200개 (댓글단 게시물, 댓글 작성자 랜덤)
    private void saveReply() {

        List<Reply> replies = new ArrayList<>();
        IntStream.rangeClosed(1, 400).forEach(i -> {
            Reply reply = Reply.builder()
                    .member(memberRepository.getReferenceById(new Random().nextLong(1, 101)))
                    .post(postRepository.getReferenceById(new Random().nextLong(1, 200)))
                    .content("testReplyContent" + i)
                    .build();
            replies.add(reply);


            if (i%2==0){
                Reply anonymousReply = Reply.builder()
                        .member(memberRepository.getReferenceById(new Random().nextLong(2, 101)))
                        .post(postRepository.getReferenceById(new Random().nextLong(1, 200)))
                        .content("testReplyContent" + i)
                        .build();

                replies.add(anonymousReply);
            }
        });

        replyRepository.saveAll(replies);
    }

    // * 테스트용 게시물 신고 -> 총 50개 (신고할 게시물, 신고자 랜덤)
    private void savePostReport(){
        List<PostReport> postReports = new ArrayList<>();
        IntStream.rangeClosed(1, 50).forEach(i -> {
            PostReport postReport = PostReport.builder()
                    .post(postRepository.getReferenceById(new Random().nextLong(1, 200)))
                    .reporter(memberRepository.getReferenceById(new Random().nextLong(2, 101)))
                    .content("Post 신고내용" + i)
                    .type(Report.Type.values()[new Random().nextInt(Report.Type.values().length)])
                    .build();

            postReports.add(postReport);
        });

        postReportRepository.saveAll(postReports);
    }


    // * 테스트용 댓글 신고 -> 총 100개 (신고할 게시물, 신고자 랜덤)
    private void saveReplyReport() {
        List<ReplyReport> replyReports = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ReplyReport replyReport = ReplyReport.builder()
                    .reply(replyRepository.getReferenceById(new Random().nextLong(1, 400)))
                    .reporter(memberRepository.getReferenceById(new Random().nextLong(2, 101)))
                    .content("Reply 신고내용" + i)
                    .type(Report.Type.values()[new Random().nextInt(Report.Type.values().length)])
                    .build();
            replyReports.add(replyReport);
        });

        replyReportRepository.saveAll(replyReports);
    }

    // * 테스트용 게시물 추천 -> 총 100개 (추천할 게시물, 추천자 랜덤)
    private void savePostRecommend() {
        List<PostRecommend> postRecommends = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            PostRecommend postRecommend = PostRecommend.builder()
                    .post(postRepository.getReferenceById(new Random().nextLong(1, 200)))
                    .member(memberRepository.getReferenceById(new Random().nextLong(2, 101)))
                    .build();
            postRecommends.add(postRecommend);
        });

        postRecommendRepository.saveAll(postRecommends);
    }

    // * 테스트용 댓글 추천 -> 총 100개 (추천할 댓글, 추천자 랜덤)
    private void saveReplyRecommend() {
        List<ReplyRecommend> replyRecommends = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ReplyRecommend replyRecommend = ReplyRecommend.builder()
                    .reply(replyRepository.getReferenceById(new Random().nextLong(1, 400)))
                    .member(memberRepository.getReferenceById(new Random().nextLong(2, 101)))
                    .build();
            replyRecommends.add(replyRecommend);
        });

        replyRecommendRepository.saveAll(replyRecommends);
    }

    // * 테스트용 게시물 북마크 -> 총 100개 (북마크할 게시물, 북마크하는 사용자 랜덤)
    private void savePostBookmark() {
        List<PostBookmark> postBookmarks = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            PostBookmark postBookmark = PostBookmark.builder()
                    .post(postRepository.getReferenceById(new Random().nextLong(1, 200)))
                    .member(memberRepository.getReferenceById(new Random().nextLong(2, 101)))
                    .build();
            postBookmarks.add(postBookmark);
        });

        postBookmarkRepository.saveAll(postBookmarks);
    }

    //* 테스트용 채팅방 -> 총 10개, 마지막 채팅방에는 메시지 X
    private void saveChatRoom() {
        List<ChatRoom> chatRooms = new ArrayList<>();
        LongStream.rangeClosed(2, 11).forEach(i -> {
            ChatRoom chatRoom;
            if (i % 2 == 0) {
                chatRoom = ChatRoom.builder()
                        .master(memberRepository.getReferenceById(1L))
                        .member(memberRepository.getReferenceById(i))
                        .build();
            } else {
                chatRoom = ChatRoom.builder()
                        .master(memberRepository.getReferenceById(i))
                        .member(memberRepository.getReferenceById(1L))
                        .build();
            }
            chatRooms.add(chatRoom);
        });
        chatRoomRepository.saveAll(chatRooms);
    }

    //* 테스트용 채팅 메시지 -> 채팅 방당 30개
    private void saveChatMessage() {
        List<ChatMessage> chatMessages = new ArrayList<>();
        LongStream.rangeClosed(2, 10).forEach(i -> {
            IntStream.rangeClosed(1, 30).forEach(j -> {
                ChatMessage chatMessage = ChatMessage.builder()
                        .chatRoom(chatRoomRepository.getReferenceById(i - 1))
                        .sender(memberRepository.getReferenceById((j % 2 == 0) ? 1L : i))
                        .content("Test message" + i)
                        .build();
                chatMessages.add(chatMessage);
            });

        });
        chatMessageRepository.saveAll(chatMessages);
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        saveSchool();
        saveMajor();
        saveMember();
        savePost();
        savePostReport();
        saveReply();
        saveReplyReport();
        savePostRecommend();
        saveReplyRecommend();
        savePostBookmark();
        saveChatRoom();
        saveChatMessage();
    }
}
