package com.sadi.hackathon_authservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sadi.hackathon_authservice.models.User;
import com.sadi.hackathon_authservice.models.requests.JwtTokenRequest;
import com.sadi.hackathon_authservice.models.requests.RegistrationRequest;
import com.sadi.hackathon_authservice.models.requests.UserVerificationRequest;
import com.sadi.hackathon_authservice.models.response.JwtTokenResponse;
import com.sadi.hackathon_authservice.service.JwtTokenService;
import com.sadi.hackathon_authservice.service.UserRegistrationAndVerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/auth")
public class AuthControllerV1 {
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRegistrationAndVerificationService userRegVerService;
    private final Environment environment;

    private final Logger logger = LoggerFactory.getLogger(AuthControllerV1.class);

    @Value("${auth.jwt.timeout}")
    private int cookieExpiration;

    @Value("${auth.jwt.cookie.name}")
    private String cookieName;

    public AuthControllerV1(JwtTokenService jwtTokenService, AuthenticationManager authenticationManager,
                            UserRegistrationAndVerificationService userRegVerService, Environment environment) {
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.userRegVerService = userRegVerService;
        this.environment = environment;
    }

    @PostMapping
    public ResponseEntity<JwtTokenResponse> generateToken(@Valid @RequestBody JwtTokenRequest tokenRequest, HttpServletResponse response) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(), tokenRequest.getPassword());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var tokenResponse = jwtTokenService.generateToken(authentication);
        Cookie cookie = new Cookie(cookieName, tokenResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(cookieExpiration);
        if(Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equalsIgnoreCase("prod"))) {
            cookie.setAttribute("SameSite", "None");
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Create a cookie with the same name, but with maxAge=0
        logger.debug("Logout request received");
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        if(Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equalsIgnoreCase("prod"))) {
            cookie.setAttribute("SameSite", "None");
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest registrationRequest) throws JsonProcessingException {
        userRegVerService.checkIfUserExists(registrationRequest.getUsername());
        logger.debug("Registered user with email={}", registrationRequest.getUsername());
        String otp = userRegVerService.getOtp();

        userRegVerService.cacheDetails(registrationRequest.getUsername(),
                registrationRequest.getPassword(), registrationRequest.getFullName(), otp);

        userRegVerService.sendVerificationEmail(registrationRequest.getUsername(), otp);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyOpt(@RequestBody UserVerificationRequest request) throws JsonProcessingException {
        User user = userRegVerService.verifyUser(request.getUsername(), request.getOtp());
        userRegVerService.saveUser(user);
        return ResponseEntity.ok().build();
    }
}
