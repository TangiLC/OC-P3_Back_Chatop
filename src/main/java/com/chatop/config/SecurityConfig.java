package com.chatop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.chatop.filter.JwtAuthenticationFilter;
import com.chatop.util.JwtUtil;

/**
 * Configuration for Spring Security, including JWT-based authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtUtil jwtUtil;

  /**
   * Constructs the SecurityConfig with required dependencies.
   *
   * @param jwtUtil Utility class for JWT operations.
   */
  public SecurityConfig(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  /**
   * Configures the SecurityFilterChain for HTTP security.
   *
   * @param http The HttpSecurity object to configure.
   * @return A configured SecurityFilterChain.
   * @throws Exception If any configuration errors occur.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> {
        auth
          // Public endpoints
          .requestMatchers(
            "/api/auth/**", // Login and registration
            "/public/**", // Public resources
            "/images/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html" // API documentation
          )
          .permitAll()
          .requestMatchers(
            "/api/rentals/**",
            "/api/messages/**",
            "/api/user/**"
            
          )
          .hasAnyRole("ADMIN","USER")
          .anyRequest()
          .authenticated();
      })
      
      .formLogin(form -> form.disable())
      // Add the custom JWT authentication filter
      .addFilterBefore(
        jwtAuthenticationFilter(),
        UsernamePasswordAuthenticationFilter.class
      );

    return http.build();
  }

  /**
   * Defines the custom JwtAuthenticationFilter as a bean.
   *
   * @return An instance of JwtAuthenticationFilter.
   */
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtUtil);
  }

  /**
   * Defines the PasswordEncoder bean for encrypting and verifying passwords.
   *
   * @return A BCryptPasswordEncoder instance.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Provides the AuthenticationManager bean required for authentication.
   *
   * @param authConfig The authentication configuration.
   * @return An AuthenticationManager instance.
   * @throws Exception If any configuration errors occur.
   */
  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authConfig
  ) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
