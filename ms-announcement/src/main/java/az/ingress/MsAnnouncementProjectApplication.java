package az.ingress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MsAnnouncementProjectApplication{

    public static void main(String[] args) {
        SpringApplication.run(MsAnnouncementProjectApplication.class, args);
    }
}
