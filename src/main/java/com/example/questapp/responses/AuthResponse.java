package com.example.questapp.responses;

import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
public class AuthResponse {
    String message;
    Long userId;
    String bearer;
    ResponseEntity<String> status;
    String refreshToken;
}
