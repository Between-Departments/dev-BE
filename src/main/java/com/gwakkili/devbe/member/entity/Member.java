package com.gwakkili.devbe.member.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(unique = true)
    private String mail;

    @Column(unique = true)
    private String nickname;

    private String password;

    private String school;

    private String major;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "member_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private MemberImage image;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    private boolean locked;

    private int reportCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM post_bookmark pb WHERE pb.member_id = member_id)")
    private int bookmarkCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM reply r WHERE r.member_id = member_id)")
    private int replyCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM post p WHERE p.member_id = member_id)")
    private int postCount;

    @Builder
    public Member(String mail, String nickname, String password, String school, String major) {
        this.mail = mail;
        this.nickname = nickname;
        this.password = password;
        this.school = school;
        this.major = major;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void addReportCount() {
        this.reportCount++;
    }

    public void setImage(MemberImage image) {
        this.image = image;
        image.setMember(this);
    }

    @RequiredArgsConstructor
    @Getter
    public enum Role {
        ROLE_USER("회원"),
        ROLE_MANAGER("관리자");
        private final String description;
    }
}
