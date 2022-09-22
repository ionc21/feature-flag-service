package com.mettle.auth.dto;

import java.time.Instant;

public record AuthenticationResponse(String authenticationToken, String refreshToken,
        Instant expiresAt, String userName) {
}
