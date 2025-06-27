package com.bytebuilder.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {

    private final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret";

    private JwtParser getParser() {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return getParser()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println(e.getMessage());
        } catch (JwtException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Claims claims = getParser().parseClaimsJws(token).getBody();
            String actualUsername = claims.getSubject();
            Date expiration = claims.getExpiration();
            return actualUsername.equals(expectedUsername) && expiration.after(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println(e.getMessage());
        } catch (JwtException e) {
            System.out.println( e.getMessage());
        }
        return false;
    }
}
