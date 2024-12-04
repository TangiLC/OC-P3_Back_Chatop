package com.chatop.filter;

import com.chatop.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Custom filter for validating JWT tokens in incoming HTTP requests.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  /**
   * Constructs the JwtAuthenticationFilter with the necessary dependencies.
   *
   * @param jwtUtil Utility class for handling JWT tokens.
   */
  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    // Extract the Authorization header
    String authorizationHeader = request.getHeader("Authorization");

    if (
      authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
    ) {
      String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
      if (jwtUtil.validateToken(token)) {
        // Extract user email (or other unique identifier) from the token
        String email = jwtUtil.extractEmail(token);

        // Create an Authentication object and set it in the security context
        Authentication auth = new UsernamePasswordAuthenticationToken(
          email,
          null,
          Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Example role
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    // Continue the filter chain
    filterChain.doFilter(request, response);
  }
}
