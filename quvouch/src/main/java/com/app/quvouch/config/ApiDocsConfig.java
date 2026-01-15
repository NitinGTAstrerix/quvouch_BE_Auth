package com.app.quvouch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Qvouch is platform to create a google review QR for shops",
                description = "This is a platform to add the generic review for the restorant, " +
                        "shops, hotels, bars, markets, etc",
                version = "1.0.0",
                contact = @Contact(
                        name = "Gtasterix IT Pvt Ltd",
                        email = "info@gtasterix.com",
                        url = "https://www.gtasterix.com/"
                )
        ),
        security = @SecurityRequirement(
                name = "BearerAuth"
        )
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class ApiDocsConfig {
}
