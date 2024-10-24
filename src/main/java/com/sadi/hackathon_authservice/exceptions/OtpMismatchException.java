package com.sadi.hackathon_authservice.exceptions;

public class OtpMismatchException extends RuntimeException{
    public OtpMismatchException(String message) {
        super(message);
    }
}
