package com.team3.ternaryoperator.common.aop;



import com.team3.ternaryoperator.common.entity.Activity;

import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.activities.repository.ActivityRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



import java.util.Optional;



@Aspect
@Slf4j
@RequiredArgsConstructor
public class TrackTimeAspect {


    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    @Around("@annotation(com.team3.ternaryoperator.common.aop.TrackTime)")
    public Object logAndSaveActivity(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();

        String className = joinPoint.getTarget().getClass().getSimpleName();

        long start = System.currentTimeMillis();

        log.info("[START] {}.{}", className, methodName);



        try {
            Object result = joinPoint.proceed();
            long time = System.currentTimeMillis() - start;
            log.info("[END] {}.{} | {}ms", className, methodName, time);
//
//            log.info("result {}", result.);
//            saveActivity(className + "." + methodName);

            return result;

        } catch (Exception e) {
            long time = System.currentTimeMillis() - start;
            log.error("[ERROR] {}.{} | {}ms | {}", className, methodName, time, e.getMessage());
            throw e;
        }

    }



    private void saveActivity(String description) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // auth가 널이 아니면서, auth가 인증이 되어있어야하고,
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            String username = null;
            log.info("debug1 ");
            /*
            if (principal instanceof User user) {
                username = user.getUsername();
                log.info("debug2");
            }

            if (username != null) {
                Optional<User> actorOptional = userRepository.findByUsername(username);
                log.info("debug3 ");
                if (actorOptional.isPresent()) {
                    User actor = actorOptional.get();
                    log.info("debug4 ");
                    Activity activity = new Activity(
                            "METHOD_EXECUTED",
                            actor,
                            null,
                            description
                    );
                    activityRepository.save(activity);
                }
            }*/
        }
    }
}