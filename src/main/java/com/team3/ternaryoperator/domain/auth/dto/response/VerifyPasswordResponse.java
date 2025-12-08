package com.team3.ternaryoperator.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class VerifyPasswordResponse {
    private final boolean valid;

    public VerifyPasswordResponse(boolean valid) {
        this.valid = valid;
    }
}
