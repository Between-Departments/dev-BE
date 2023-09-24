package com.gwakkili.devbe.dto;

import com.gwakkili.devbe.entity.Member.Member;
import com.gwakkili.devbe.entity.Member.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberDto implements UserDetails {

    private long memberId;

    private String mail;

    private String password;

    private String nickname;

    private String school;

    private String major;

    private boolean locked;

    private Set<Role> roles = new HashSet<>();

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    public static MemberDto of(Member member){
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .mail(member.getMail())
                .school(member.getSchool().getName())
                .major(member.getMajor().getName())
                .locked(member.isLocked())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .roles(member.getRoles())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
