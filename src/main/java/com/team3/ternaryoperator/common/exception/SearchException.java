package com.team3.ternaryoperator.common.exception;

import lombok.Getter;

@Getter
public class SearchException extends CustomException {
    public SearchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
