package com.chatop.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.chatop.exception.JwtValidationException;

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
    this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    this.expiration = 3600000;
  }

  /**
   * Génère un token JWT contenant l'email et le rôle de l'utilisateur.
   *
   * @param email L'email de l'utilisateur.
   * @param role  Le rôle de l'utilisateur.
   * @return Le token JWT.
   */
  public String generateToken(String email, String role) {
    return Jwts
      .builder()
      .setSubject(email)
      .claim("role", role)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(secretKey)
      .compact();
  }

  /**
   * Valide un token JWT.
   *
   * @param token Le token JWT.
   * @return true si le token est valide, sinon false.
   * @throws JwtValidationException En cas d'erreur de validation du token.
   */
  public boolean validateToken(String token) {
    try {
      JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
      parser.parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new JwtValidationException("Token expired", e);
    } catch (MalformedJwtException e) {
      throw new JwtValidationException("Token malformed", e);
    } catch (UnsupportedJwtException e) {
      throw new JwtValidationException("Token unsupported", e);
    } catch (JwtException e) {
      throw new JwtValidationException("Token validation error", e);
    }
  }

  /**
   * Extrait la date d'expiration du token JWT.
   *
   * @param token Le token JWT.
   * @return La date d'expiration.
   * @throws JwtValidationException En cas d'erreur de validation du token.
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
      throw new JwtValidationException(
        "Token expired while extracting expiration",
        e
      );
    } catch (JwtException e) {
      throw new JwtValidationException(
        "Error extracting expiration from token",
        e
      );
    }
  }

  /**
   * Extrait l'email de l'utilisateur depuis le token JWT.
   *
   * @param token Le token JWT.
   * @return L'email de l'utilisateur.
   * @throws JwtValidationException En cas d'erreur de validation du token.
   */
  public String extractEmail(String token) {
    try {
      Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
      return claims.getSubject();
    } catch (ExpiredJwtException e) {
      throw new JwtValidationException(
        "Token expired while extracting email",
        e
      );
    } catch (JwtException e) {
      throw new JwtValidationException("Error extracting email from token", e);
    }
  }

  /**
   * Extrait le rôle de l'utilisateur depuis le token JWT.
   *
   * @param token Le token JWT.
   * @return Le rôle de l'utilisateur.
   * @throws JwtValidationException En cas d'erreur de validation du token.
   */
  public String extractRole(String token) {
    try {
      Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
      return claims.get("role", String.class);
    } catch (JwtException e) {
      throw new JwtValidationException("Error extracting role from token", e);
    }
  }
}
