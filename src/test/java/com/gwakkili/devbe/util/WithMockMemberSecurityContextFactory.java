package com.gwakkili.devbe.util;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.security.dto.MemberDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockMember> {
    @Override
    public SecurityContext createSecurityContext(WithMockMember customMember) {

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Set<Member.Role> roles = Arrays.stream(customMember.roles()).map(Member.Role::valueOf).collect(Collectors.toSet());
        UserDetails principal = MemberDetails.builder().mail(customMember.mail()).password(customMember.password()).roles(roles).build();
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
