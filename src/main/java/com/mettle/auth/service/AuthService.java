package com.mettle.auth.service;

import com.mettle.auth.dto.AuthenticationResponse;
import com.mettle.auth.dto.LoginRequest;
import com.mettle.auth.dto.RegisterRequest;
import com.mettle.auth.security.JwtProvider;
import com.mettle.model.Role;
import com.mettle.model.User;
import com.mettle.repository.RoleRepository;
import com.mettle.repository.UserRepository;
import com.mettle.transactionwrapper.TransactionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final TransactionWrapper transactionWrapper;

    public void signup(RegisterRequest registerRequest) {
        transactionWrapper.runInNewTransaction(entityManager ->  persistUser(registerRequest));
    }

    private void persistUser(RegisterRequest registerRequest) {
        String requestRole = registerRequest.getRole();
        Role role = roleRepository.findByName(requestRole)
                .orElseThrow(() -> new EntityNotFoundException("Role does not exist - " +requestRole));
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .userName(registerRequest.getUserName())
                .roles(Set.of(role))
                .features(new HashSet<>())
                .password(passwordEncoder.encode(registerRequest.getPassword())).build();
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .userName(loginRequest.getUserName())
                .build();
    }
}