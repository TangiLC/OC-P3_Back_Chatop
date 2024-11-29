package com.chatop.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  private final SecretKey secretKey;
  private final long expiration;

  public JwtUtil() {
    // Génère une clé sécurisée dynamique pour HMAC-SHA-256
    this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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
    return Jwts
      .builder()
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
      JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
      parser.parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      System.err.println("expired Token: " + e.getMessage());
    } catch (MalformedJwtException e) {
      System.err.println("malformed Token: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.err.println("unsupported Jwt: " + e.getMessage());
    } catch (JwtException e) {
      System.err.println("JWT treatment exception: " + e.getMessage());
    }
    return false;
  }

  /**
   * Extrait la date d'expiration du token JWT.
   *
   * @param token Le token JWT.
   * @return La date d'expiration ou null si le token est invalide.
   */
  public Date extractExpiration(String token) {
    try {
      return Jwts
        .parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
    } catch (ExpiredJwtException e) {
      System.err.println("expired Token @expExtract: " + e.getMessage());
    } catch (JwtException e) {
      System.err.println("JWT Token expiration date error: " + e.getMessage());
    }
    return null;
  }

  /**
   * Extrait l'email de l'utilisateur à partir d'un token JWT.
   *
   * @param token Le token JWT.
   * @return L'email de l'utilisateur ou null si le token est invalide.
   */
  public String extractEmail(String token) {
    try {
      JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
      Claims claims = parser.parseClaimsJws(token).getBody();
      return claims.getSubject();
    } catch (ExpiredJwtException e) {
      System.err.println("expired Token @mailExtract: " + e.getMessage());
    } catch (JwtException e) {
      System.err.println("JWT Token email error: " + e.getMessage());
    }
    return null;
  }
}
