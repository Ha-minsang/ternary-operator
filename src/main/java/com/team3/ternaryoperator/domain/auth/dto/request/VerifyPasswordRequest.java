package com.team3.ternaryoperator.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyPasswordRequest {
    private String password;
}
