package com.mettle.auth.service;

import com.mettle.auth.model.RefreshToken;
import com.mettle.auth.repository.RefreshTokenRepository;
import com.mettle.transactionwrapper.TransactionWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TransactionWrapper transactionWrapper;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString()).build();

        return transactionWrapper.runFunctionInNewTransaction(entityManager ->  refreshTokenRepository.save(refreshToken));
    }
}
