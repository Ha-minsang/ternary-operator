package com.team3.ternaryoperator.common.exception;

import lombok.Getter;

@Getter
public class TeamException extends CustomException {
    public TeamException(ErrorCode errorCode) {
        super(errorCode);
    }
}
