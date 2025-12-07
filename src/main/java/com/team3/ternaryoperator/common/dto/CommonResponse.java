package com.team3.ternaryoperator.common.dto;

import com.team3.ternaryoperator.common.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommonResponse<T> {

    private final boolean success;         // 성공 여부
    private final String code;             // 성공시 SUCCESS , 실패시 CustomErrorCode
    private final String message;          // 성공시 요청이 성공했습니다. , 실패시 error message
    private final String path;             // 실패 시만 사용
    private final LocalDateTime timestamp; // 실패 시만 사용
    private final T data;                  // 성공 시만 사용

    private CommonResponse(boolean success, String code, String message,
                           String path, LocalDateTime timestamp, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
        this.data = data;
    }

    // 성공시 공용 응답 객체
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, "SUCCESS", "요청이 성공했습니다.",
                null, null, data);
    }

    // 실패시 공용 응답 객체
    public static <T> CommonResponse<T> fail(ErrorCode errorCode, String path) {
        return new CommonResponse<>(false, errorCode.getCode(), errorCode.getMessage(),
                path, LocalDateTime.now(), null);
    }

    // 메세지를 직접 입력하는 경우
    public static <T> CommonResponse<T> fail(ErrorCode errorCode, String message, String path) {
        return new CommonResponse<>(
                false,
                errorCode.getCode(),
                message,
                path,
                LocalDateTime.now(),
                null
        );
    }
}

