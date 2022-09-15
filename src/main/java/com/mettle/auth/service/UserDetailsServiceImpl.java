package com.mettle.auth.service;

import com.mettle.model.Role;
import com.mettle.model.User;
import com.mettle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user Found with userName : " + userName));

        return new org.springframework.security
                .core.userdetails.User(user.getUserName(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }
}
