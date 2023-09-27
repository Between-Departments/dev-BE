package com.gwakkili.devbe.security.dto;

import com.gwakkili.devbe.entity.Member;
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
public class MemberDetails implements UserDetails {

    private long memberId;

    private String mail;

    private String password;

    private boolean locked;

    private Set<Member.Role> roles = new HashSet<>();

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private LocalDateTime deleteAt;

    public static MemberDetails of(Member member){
        return MemberDetails.builder()
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .mail(member.getMail())
                .locked(member.isLocked())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .deleteAt(member.getDeleteAt())
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
        return deleteAt == null;
    }
}
