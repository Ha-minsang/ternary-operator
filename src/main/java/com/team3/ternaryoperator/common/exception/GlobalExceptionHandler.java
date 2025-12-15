package com.team3.ternaryoperator.common.exception;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<Void>> handleException(CustomException e) {
        log.error("예외 발생. ", e);
        CommonResponse<Void> response = CommonResponse.fail(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("예외 발생. ", e);

        // FieldError 메시지 추출
        String detailMessage = e.getFieldError() != null
                ? e.getFieldError().getDefaultMessage()
                : "";

        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        String finalMessage = errorCode.getMessage() + detailMessage;

        CommonResponse<Void> response = CommonResponse.fail(errorCode, finalMessage);
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

}

