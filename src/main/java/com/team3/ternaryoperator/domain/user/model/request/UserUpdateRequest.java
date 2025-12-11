package com.team3.ternaryoperator.domain.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {

    private String name;
    private String email;
    private String password;
}
