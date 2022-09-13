package com.mettle.security;

import com.mettle.security.exception.TokenGenerationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;

import static io.jsonwebtoken.Jwts.parserBuilder;
import static java.util.Date.from;

@Service
public class JwtProvider {

    private KeyStore keyStore;
    private final Long jwtExpirationInMillis;
    private final String keyStorePath;
    private final String keyStorePassword;
    private final String keyAlias;
    private final String privateKeyPassphrase;

    public JwtProvider(@Value("${jwt.expiration.time}") Long jwtExpirationInMillis,
                       @Value("${app.security.jwt.keystore.location}") String keyStorePath,
                       @Value("${app.security.jwt.keystore.password}") String keyStorePassword,
                       @Value("${app.security.jwt.key-alias}") String keyAlias,
                       @Value("${app.security.jwt.private-key-passphrase}") String privateKeyPassphrase) {
        this.jwtExpirationInMillis = jwtExpirationInMillis;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
        this.keyAlias = keyAlias;
        this.privateKeyPassphrase = privateKeyPassphrase;
    }

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream(keyStorePath);
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new TokenGenerationException("Exception occurred while loading keystore", e);
        }
    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(keyAlias, privateKeyPassphrase.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new TokenGenerationException("Exception occurred while retrieving public key from keystore", e);
        }
    }

    public boolean validateToken(String jwt) {
        parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (KeyStoreException e) {
            throw new TokenGenerationException("Exception occurred while retrieving public key from keystore", e);
        }
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
