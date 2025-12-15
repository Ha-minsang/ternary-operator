package com.team3.ternaryoperator.common.exception;

import lombok.Getter;

@Getter
public class TaskException extends CustomException {
    public TaskException(ErrorCode errorCode) {
        super(errorCode);
    }
}
