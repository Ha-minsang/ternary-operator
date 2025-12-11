package com.team3.ternaryoperator.common.aspect;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.domain.auth.dto.request.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@annotation(com.team3.ternaryoperator.common.aspect.ActivityLog)")
    public Object logActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean exception = false;
        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            exception = true;
        }
        String username = getCaller(joinPoint);
        long end = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        log.info(" 활동 로그 | 실행 시간={}ms | 호출자={} | 메서드명 = {}.{} | 입력 파라미터={} | 실행 결과={} | 예외 발생={}",
                (end - start),
                username,
                className,
                methodName,
                joinPoint.getArgs(),
                result,
                exception
        );
        return result;
    }

    private String getCaller(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof AuthUser authUser) {
                return authUser.getUsername();
            }
        }
        for (Object arg : args) {
            if (arg instanceof LoginRequest loginRequest) {
                return loginRequest.getUsername();
            }
        }
        return null;
    }
}
