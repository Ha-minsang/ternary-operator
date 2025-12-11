package com.team3.ternaryoperator.common.aop;


import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Activity;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.activities.repository.ActivityRepository;
import com.team3.ternaryoperator.domain.comment.model.response.CommentResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskResponse;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import com.team3.ternaryoperator.common.exception.CustomException;
import static com.team3.ternaryoperator.common.exception.ErrorCode.USER_NOT_FOUND;


@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class TrackTimeAspect {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    // 메소드 기반 포인트 컷
    @Pointcut("@annotation( com.team3.ternaryoperator.common.aop.TrackTime )")
    public void TrackTimePointCut() {
    }

    // 메인 Around
    @Around("TrackTimePointCut()")
    public Object Aop(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); // 메소드 타입을 받음

        TrackTime trackTime = methodSignature.getMethod().getAnnotation(TrackTime.class); //
        trackTime.type(); // 직접 준 타입을 받아올수있다. Activity 엔티티 내 type 변수로 받아올수있다

        try {
            Object result = joinPoint.proceed(); // 주 처리

            joinPoint.getArgs(); // 메소드의 매게변수 받아옴
            methodSignature.getParameterNames(); // 파라미터의 이름을 받아올수있다.


            // 선언
            Long userId =  ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            Long targetId = null;
            String description = "";

            // 시큐리티 컨태스트 홀더 공부하기
            // description TASK
            if (trackTime.type().equals("TASK_CREATED")) {
                description = "새 작업을 생성했습니다.";
            } else if (trackTime.type().equals("TASK_UPDATED")) {
                description = "작업을 수정했습니다.";
            } else if (trackTime.type().equals("TASK_DELETED")) {
                description = "작업을 삭제했습니다.";
            } else if (trackTime.type().equals("TASK_STATUS_CHANGED")) {
                description = "작업의 상태를 변경했습니다.";
            }

            // description COMMENT
            if (trackTime.type().equals("COMMENT_CREATED")) {
                description = "새 댓글을 생성했습니다.";
            } else if (trackTime.type().equals("COMMENT_UPDATED")) {
                description = "댓글을 수정했습니다.";
            } else if (trackTime.type().equals("COMMENT_DELETED")) {
                description = "댓글을 삭제했습니다.";
            }


            for (int i = 0; i < methodSignature.getParameterNames().length; i++) { // for문으로 userId가 제대로 가져온게 맞는지 체크

               if (methodSignature.getParameterNames()[i].equals("taskId") ) { // 맞는지 체크 하고 taskId 가져옴
                    targetId = (Long) joinPoint.getArgs()[i];
               }
               if (methodSignature.getParameterNames()[i].equals("commentId") ) { // 맞는지 체크하고 commentId 가져옴
                   targetId = (Long) joinPoint.getArgs()[i];
               }
            }

            User user = userRepository.findById(userId).orElseThrow( // 유저id로 찾기
                    () -> new CustomException(USER_NOT_FOUND)
            );

            if (trackTime.type().equals("TASK_CREATED")) { // CREATED에는 taskId가 없음
                targetId = ((TaskResponse) result).getId(); // Task Id를 받아올수있음.!*
            } else if (trackTime.type().equals("COMMENT_CREATED")) { // commentId가 없음
               targetId = ((CommentResponse) result).getId(); // Comment ID를 받을수 있다.!
            }

            activityRepository.save(new Activity(trackTime.type(),user ,targetId, description)); // 저장

            log.info("저장성공했습니다."); // << - AfterRunning 방식으로 성공햇을때만 출력.

            return result;
        } catch (RuntimeException exception) {
            throw new Exception();

        }
    }
}