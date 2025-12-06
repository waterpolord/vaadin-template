package com.robertgarcia.template.shared.service;


import com.robertgarcia.template.modules.users.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SignatureException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class JwtUtil {

    private final String SECRET = "U2xhc2hpbmctVW53YXRjaGVkLUJ1bmdlZS1DYWNrbGU2LUltbW9yYWxseS1JbXByaXNvbi1DaW5jaC1TdHJ1bQ==";

    public boolean validateJwtToken(String authToken) throws SignatureException {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            Logger.getLogger(JwtUtil.class.getName()).log(Level.SEVERE,"Invalid JWT token", e);

        } catch (ExpiredJwtException e) {
            Logger.getLogger(JwtUtil.class.getName()).log(Level.SEVERE,"JWT token is expired", e);

        } catch (UnsupportedJwtException e) {
            Logger.getLogger(JwtUtil.class.getName()).log(Level.SEVERE,"JWT token is unsupported", e);

        } catch (IllegalArgumentException e) {
            Logger.getLogger(JwtUtil.class.getName()).log(Level.SEVERE,"JWT claims string is empty", e);
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 54000000)) //3 horas 10800000
                .signWith(getSigningKey())
                .compact();
    }
}
