package com.chatop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

/**
 * Configuration for OpenAPI/Swagger documentation.
 * Default url =http://localhost:3001/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

  /**
   * Customizes the OpenAPI documentation with application-specific information.
   *
   * @return an OpenAPI instance with metadata.
   */
  @Bean
  public OpenAPI customOpenAPI() {
    SecurityScheme securityScheme = new SecurityScheme()
      .type(Type.HTTP)
      .scheme("bearer")
      .bearerFormat("JWT")
      .description("Enter the JWT token to access secured endpoints");

    SecurityRequirement securityRequirement = new SecurityRequirement()
      .addList("bearerAuth");

    return new OpenAPI()
      .info(
        new Info()
          .title("Chatop Rental API")
          .version("1.0.0")
          .description(
            """
            API documentation for the Chatop Rental project *(Open Classrooms Full-Stack Java Angular Project 3)*
            \n⚠️Get the JWT Authorize token from route **auth/register** or **auth/login** to access secured endpoints.
          """
          )
      )
      .addSecurityItem(securityRequirement)
      .components(
        new io.swagger.v3.oas.models.Components()
          .addSecuritySchemes("bearerAuth", securityScheme)
      )
      ;
  }
}
