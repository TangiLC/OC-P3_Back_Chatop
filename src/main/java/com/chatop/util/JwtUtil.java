package com.chatop.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtUtil() {
        // Génère une clé sécurisée dynamique pour HMAC-SHA-512
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // Définit la durée d'expiration du token (par exemple, 1 heure)
        this.expiration = 3600000; // 1 heure en millisecondes
    }

    /**
     * Génère un token JWT.
     *
     * @param email L'email de l'utilisateur.
     * @return Le token JWT.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey) // Utilise la clé générée
                .compact();
    }

    /**
     * Valide un token JWT.
     *
     * @param token Le token JWT.
     * @return true si le token est valide, sinon false.
     */
    public boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secretKey) // Utilise la clé générée
                    .build();
            parser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Date extractExpiration(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (Exception e) {
            return null; 
        }
    }

    /**
     * Extrait l'email de l'utilisateur à partir d'un token JWT.
     *
     * @param token Le token JWT.
     * @return L'email de l'utilisateur.
     */
    public String extractEmail(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey) // Utilise la clé générée
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
