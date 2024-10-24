package com.sadi.hackathon_authservice.service;

import com.sadi.hackathon_authservice.models.User;
import com.sadi.hackathon_authservice.models.response.JwtTokenResponse;
import com.sadi.hackathon_authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JwtTokenService {
    private final JwtEncoder jwtEncoder;

    @Value("${auth.jwt.audiences}")
    private String[] audiences;

    @Value("${auth.jwt.timeout}")
    private int timeout;

    @Value("${auth.jwt.issuer}")
    private String issuer;
    private final UserRepository userRepository;

    public JwtTokenService(JwtEncoder jwtEncoder,
                           UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
    }

    public JwtTokenResponse generateToken(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException(authentication.getName())
        );
        var claims = JwtClaimsSet.builder()
                                .issuer(issuer)
                                .issuedAt(Instant.now())
                                .audience(List.of(audiences))
                                .expiresAt(Instant.now().plusSeconds(timeout))
                                .subject(user.getId().toString())
                                .build();
        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new JwtTokenResponse(token, user.getId(), user.getUsername());
    }
}