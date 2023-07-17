package com.example.questapp.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${questapp.app.secret}")
    private String APP_SECRET;

    @Value("${questapp.expires.in}")
    private long EXPIRES_IN;

    public String generateJwtToken(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET.getBytes())
                .compact();
    }

    public String generateJwtTokenByUserId(Long userId) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET.getBytes())
                .compact();
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(APP_SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(APP_SECRET.getBytes())
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException
                 | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(APP_SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}