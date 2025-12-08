package com.team3.ternaryoperator.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String accessToken;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
