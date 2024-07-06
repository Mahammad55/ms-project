package az.ingress.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {
    private final AppConfig appConfig;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties properties = javaMailSender.getJavaMailProperties();

        javaMailSender.setProtocol(appConfig.getProtocol());
        javaMailSender.setHost(appConfig.getHost());
        javaMailSender.setPort(Integer.parseInt(appConfig.getPort()));
        javaMailSender.setUsername(appConfig.getUsername());
        javaMailSender.setPassword(appConfig.getPassword());

        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "false");

        return javaMailSender;
    }
}
