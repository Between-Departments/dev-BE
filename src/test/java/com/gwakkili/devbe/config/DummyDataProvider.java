package com.gwakkili.devbe.config;

import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.major.repository.MajorRepository;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.repository.PostRepository;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.repository.ReplyRepository;
import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.report.entity.Report;
import com.gwakkili.devbe.report.repository.ReplyReportRepository;
import com.gwakkili.devbe.shcool.entity.School;
import com.gwakkili.devbe.shcool.repository.SchoolRepository;
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

@TestComponent
public class DummyDataProvider implements ApplicationRunner {

    private SchoolRepository schoolRepository;

    private MajorRepository majorRepository;

    private MemberRepository memberRepository;

    private PostRepository postRepository;

    private ReplyRepository replyRepository;

    private ReplyReportRepository replyReportRepository;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DummyDataProvider() {
    }

    @Autowired
    public DummyDataProvider(SchoolRepository schoolRepository,
                             MajorRepository majorRepository,
                             MemberRepository memberRepository,
                             PostRepository postRepository,
                             ReplyRepository replyRepository,
                             ReplyReportRepository replyReportRepository) {
        this.schoolRepository = schoolRepository;
        this.majorRepository = majorRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.replyReportRepository = replyReportRepository;
    }

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

    private void saveMajor(){
        List<Major> majors = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i->{
            Major major = Major.builder().
                    category(Major.Category.values()[new Random().nextInt(Major.Category.values().length)])
                    .name("테스트학과" + i)
                    .build();
            majors.add(major);
        });
        majorRepository.saveAll(majors);
    }

    private void saveMember(){

        Member member = Member.builder()
                .mail("test@test1.ac.kr")
                .nickname("테스트멤버")
                .password(passwordEncoder.encode("a12341234!"))
                .major("테스트학과1")
                .school("테스트대학1")
                .build();
        member.addRole(Member.Role.ROLE_MANAGER);
        member.setImage(MemberImage.builder().url("http://test.com/images/image.jpg").build());
        memberRepository.save(member);

        List<Member> members = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i->{
            Member member2 = Member.builder()
                    .mail("test"+i+"@test" + new Random().nextInt(1, 10) + ".ac.kr")
                    .nickname("테스트멤버" + i)
                    .password(passwordEncoder.encode("a12341234!"))
                    .major("테스트학과" + new Random().nextInt(1, 10))
                    .school("테스트대학" + new Random().nextInt(1, 10))
                    .build();
            member2.addRole(Member.Role.ROLE_USER);
            member2.setImage(MemberImage.builder().url("http://test.com/images/image" + i + ".jpg").build());
            members.add(member2);
        });
        memberRepository.saveAll(members);
    }

    private void savePost() {
        Post post = Post.builder()
                .title("testTitle")
                .content("testContent")
                .category(Post.Category.DAILY)
                .major("테스트학과1")
                .writer(Member.builder().memberId(1).build())
                .build();
        postRepository.save(post);
    }

    private void saveReply() {

        List<Reply> replies = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Reply reply = Reply.builder()
                    .member(Member.builder().memberId(1).build())
                    .post(postRepository.getReferenceById(1l))
                    .content("testReplyContent" + i)
                    .isAnonymous(false)
                    .build();
            replies.add(reply);
        });
        replyRepository.saveAll(replies);

    }

    private void saveReplyReport() {
        List<ReplyReport> replyReports = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            ReplyReport replyReport = ReplyReport.builder()
                    .reply(Reply.builder().replyId(new Random().nextInt(1, 10)).build())
                    .reporter(Member.builder().memberId(1).build())
                    .content("신고내용" + i)
                    .Type(Report.Type.PORNOGRAPHY)
                    .build();
            replyReports.add(replyReport);
        });
        replyReportRepository.saveAll(replyReports);
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveSchool();
        saveMajor();
        saveMember();
        savePost();
        saveReply();
        saveReplyReport();
    }

}
