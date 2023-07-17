package com.example.questapp.controller;

import com.example.questapp.entity.RefreshToken;
import com.example.questapp.entity.User;
import com.example.questapp.requests.RefreshRequest;
import com.example.questapp.requests.UserRequest;
import com.example.questapp.responses.AuthResponse;
import com.example.questapp.security.JwtTokenProvider;
import com.example.questapp.service.RefreshTokenService;
import com.example.questapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest loginRequest){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        User user = userService.getUserByUsername(loginRequest.getUsername());

        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login successfully.");
        authResponse.setBearer(jwtToken);
        authResponse.setUserId(user.getId());
        authResponse.setStatus(ResponseEntity.status(HttpStatus.OK).body(""));
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));

        return authResponse;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest registerRequest){
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        AuthResponse authResponse = new AuthResponse();
        User user = new User();
        if (userService.getUserByUsername(username) != null) {
            authResponse.setMessage("Username already in use");
            authResponse.setStatus(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BAD_REQUEST"));
            return new ResponseEntity<>(authResponse,HttpStatus.BAD_REQUEST);
        }

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveOneUser(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        authResponse.setMessage("User successfully registered.");
        authResponse.setBearer(jwtToken);
        authResponse.setUserId(user.getId());
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authResponse.setStatus(ResponseEntity.status(HttpStatus.CREATED).body(""));

        return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest){
        AuthResponse response = new AuthResponse();
        RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
        if(token.getToken().equals(refreshRequest.getRefreshToken()) &&
                !refreshTokenService.isRefreshExpired(token)){

            User user = token.getUser();
            String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());

            response.setMessage("Token successfully refresh.");
            response.setBearer(jwtToken);
            response.setUserId(user.getId());
            response.setStatus(ResponseEntity.status(HttpStatus.OK).body("OK"));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            response.setMessage("Refresh token is not valid.");
            response.setStatus(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED"));
            return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }
    }
}
