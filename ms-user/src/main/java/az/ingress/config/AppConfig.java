package az.ingress.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class AppConfig {
    private String protocol;

    private String host;

    private String port;

    private String username;

    private String password;
}