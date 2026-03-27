package com.bookshelf.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleOAuthConfig {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    // Exposes a single shared GoogleIdTokenVerifier bean.
    // AuthService injects this instead of creating a new verifier on every request.
    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
        .setAudience(Collections.singletonList(googleClientId))
        .build();
    }
}
