package com.team3.ternaryoperator.common.exception;

import lombok.Getter;

@Getter
public class CommentException extends CustomException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
