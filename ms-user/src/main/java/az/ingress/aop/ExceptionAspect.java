package az.ingress.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ExceptionAspect {
    @AfterThrowing("execution(* az.ingress.service.impl.UserDetailsServiceImpl.loadUserByUsername(String))")
    public void userNotFoundAspect(JoinPoint point) {
        log.error("UserDetailsService.loadUserByUsername.error -- user not found username:{}", point.getArgs()[0]);
    }
}
