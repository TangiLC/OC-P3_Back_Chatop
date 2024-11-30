package com.chatop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

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
        return new OpenAPI()
                .info(new Info()
                        .title("Chatop Rental API")
                        .version("1.0.0")
                        .description("API documentation for the Chatop Rental project"));
    }
}
