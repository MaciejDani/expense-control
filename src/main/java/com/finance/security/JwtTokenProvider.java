package com.finance.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Generating token for username: " + username);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            System.out.println("Extracted username from token: " + username);
            return username;
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
            return null;
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
            return null;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
            return null;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
            return null;
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
            return null;
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            System.out.println("Invalid JWT token");
            return false;
        }
    }
}