package com.sadi.hackathon_authservice.models.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtTokenRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
