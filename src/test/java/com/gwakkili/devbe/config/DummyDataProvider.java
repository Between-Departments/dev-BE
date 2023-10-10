package com.gwakkili.devbe.config;

import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.shcool.entity.School;
import com.gwakkili.devbe.mail.repository.MailAuthCodeRepository;
import com.gwakkili.devbe.major.repository.MajorRepository;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.shcool.repository.SchoolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestComponent;
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


    private PasswordEncoder passwordEncoder;

    public DummyDataProvider() {
    }

    @Autowired
    public DummyDataProvider(SchoolRepository schoolRepository,
                             MajorRepository majorRepository,
                             MemberRepository memberRepository,
                             MailAuthCodeRepository mailAuthCodeRepository,
                             PasswordEncoder passwordEncoder) {
        this.schoolRepository = schoolRepository;
        this.majorRepository = majorRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveSchool();
        saveMajor();
        saveMember();
    }

}
