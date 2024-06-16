package az.ingress.aop;

import az.ingress.model.dto.request.LoginRequest;
import az.ingress.model.dto.request.RegisterRequest;
import az.ingress.exception.AlreadyExistException;
import az.ingress.exception.PasswordIncorrectException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Around("execution(* az.ingress.service.impl.UserServiceImpl.*(..))")
    public Object elapsedTimeLogger(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();

        String fullClassName = String.valueOf(method.getDeclaringClass());
        String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        String methodName = method.getName();

        String email = null;
        for (Object arg : args) {
            if (arg instanceof RegisterRequest) {
                email = ((RegisterRequest) arg).getEmail();
            } else if (arg instanceof LoginRequest) {
                email = ((LoginRequest) arg).getEmail();
            }
        }

        log.info("{}.{}.start email:{}", className, methodName, email);

        Object proceed;
        try {
            proceed = point.proceed();
        } catch (AlreadyExistException exception) {
            log.error("{}.{}.error -- user already exists:{}", className, methodName, email);
            throw exception;
        } catch (PasswordIncorrectException exception) {
            log.error("{}.{}.error -- password mismatch for user:{}", className, methodName, email);
            throw exception;
        } catch (Exception exception) {
            log.error("{}.{}.error -- an unexpected error occurred for user:{}", className, methodName, email, exception);
            throw exception;
        }

        long endTime = System.currentTimeMillis();
        log.info("{}.{}.success in milliseconds:{}", className, methodName, endTime - startTime);

        return proceed;
    }
}
