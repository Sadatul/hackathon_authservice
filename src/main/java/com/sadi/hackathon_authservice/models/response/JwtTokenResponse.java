package com.sadi.hackathon_authservice.models.response;

public record JwtTokenResponse(String token, Long userId, String username) {
}
