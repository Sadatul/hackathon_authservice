package com.sadi.hackathon_authservice.models.dtos;

import com.sadi.hackathon_authservice.models.User;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UnverifiedUser implements Serializable {
    private User user;
    private String otp;
}