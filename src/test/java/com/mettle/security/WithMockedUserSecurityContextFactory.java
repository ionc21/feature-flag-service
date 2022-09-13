package com.mettle.security;

import com.mettle.model.Role;
import com.mettle.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class WithMockedUserSecurityContextFactory implements WithSecurityContextFactory<WithMockedUser> {

    @Override
    public SecurityContext createSecurityContext(final WithMockedUser mockedUser) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        Set<Role> roles = Arrays.stream(mockedUser.role()).map(role -> Role.builder().name(role).build()).collect(Collectors.toSet());
        context.setAuthentication(new UsernamePasswordAuthenticationToken(convertUser(mockedUser, roles), null, getAuthorities(roles)));
        return context;
    }

    private User convertUser(final WithMockedUser mockedUser, Set<Role> roles) {
        return User.builder()
                .roles(roles)
                .id(mockedUser.id())
                .userName(mockedUser.userName()).build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }
}
