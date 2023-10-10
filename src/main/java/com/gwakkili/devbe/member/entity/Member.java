package com.gwakkili.devbe.member.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.image.entity.MemberImage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "member_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.ALL)
    private MemberImage image;

    private boolean locked;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM post_bookmark pb WHERE pb.member_id = member_id)")
    private Integer bookmarkCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM reply r WHERE r.member_id = member_id)")
    private Integer replyCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(1) FROM post p WHERE p.member_id = member_id)")
    private Integer postCount;

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
        ;
    }
}
