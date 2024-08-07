package az.ingress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
public class MsAnnouncementProjectApplication{

    public static void main(String[] args) {
        SpringApplication.run(MsAnnouncementProjectApplication.class, args);
    }
}
