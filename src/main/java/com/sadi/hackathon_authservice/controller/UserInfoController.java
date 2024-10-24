package com.sadi.hackathon_authservice.controller;

import com.sadi.hackathon_authservice.models.User;
import com.sadi.hackathon_authservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserInfoController {
    private final UserRepository userRepository;

    public UserInfoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/private/info/{id}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("email", user.getUsername());
        map.put("fullName", user.getFullName());

        return ResponseEntity.ok(map);
    }
}
