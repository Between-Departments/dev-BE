package com.gwakkili.devbe.security.dto;

import com.gwakkili.devbe.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Data
@Builder
public class MemberDetails implements UserDetails {

    private long memberId;

    private String mail;

    private String nickname;

    private String password;

    private boolean locked;

    private Set<Member.Role> roles;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;


    public static MemberDetails of(Member member){
        return MemberDetails.builder()
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .mail(member.getMail())
                .nickname(member.getNickname())
                .locked(member.isLocked())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .roles(member.getRoles())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
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
