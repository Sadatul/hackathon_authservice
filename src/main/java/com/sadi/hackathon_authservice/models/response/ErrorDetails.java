package com.sadi.hackathon_authservice.models.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorDetails {
    private LocalDate timeStamp;
    private String message;
    private String details;
}
