package az.ingress.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(
        title = "Bank Application API",
        description = "API for customers' bank operations",
        version = "v1.0",
        termsOfService = "Term of service (in url format)",
        contact = @Contact(
                name = "Mahammad Ilyazov",
                email = "ilyazovmehemmed@gmail.com",
                url = "https://github.com/Mahammad55"
        )
))

@SecurityScheme(
        name = "BearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
