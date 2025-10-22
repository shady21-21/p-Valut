package com.debu.prescriptoVault.config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil{

    private final Key key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}")String secret,
                   @Value("${jwt.expiration-ms}")
                   long expirationMs){
        this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs=expirationMs;
    }

    public String generateToken(String username){
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expirationMs))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch(Exception e){
            return null;
        }
    }

    public boolean validate(String token){
        try{
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
