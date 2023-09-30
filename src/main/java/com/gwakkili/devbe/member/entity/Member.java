package com.gwakkili.devbe.member.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="member_role", joinColumns = @JoinColumn(name= "member_id"))
    private Set<Role> roles = new HashSet<>();

    private String imageUrl;

    private boolean locked;

    private LocalDateTime deleteAt;

    public void addRole(Role role){
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

    @RequiredArgsConstructor
    @Getter
    public enum Role {
        ROLE_USER("회원"),
        ROLE_MANAGER("관리자");
        private final String description;;
    }
}
