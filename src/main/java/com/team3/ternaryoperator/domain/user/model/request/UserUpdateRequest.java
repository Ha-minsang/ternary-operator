package com.team3.ternaryoperator.domain.user.model.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {

    private String name;
    private String email;
    private String password;
}
