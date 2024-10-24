package com.sadi.hackathon_authservice.models.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserVerificationRequest {
    @NotNull
    private String username;
    @NotNull
    private String otp;

}