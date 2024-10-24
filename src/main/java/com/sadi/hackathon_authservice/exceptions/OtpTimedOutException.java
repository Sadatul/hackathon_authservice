package com.sadi.hackathon_authservice.exceptions;

public class OtpTimedOutException extends RuntimeException{
    public OtpTimedOutException(String message) {
        super(message);
    }
}
