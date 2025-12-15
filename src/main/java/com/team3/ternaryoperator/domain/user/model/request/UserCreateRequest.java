package com.team3.ternaryoperator.domain.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username은 영문과 숫자만 사용할 수 있습니다.")
    private String username;

    @NotBlank
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$", message = "password는 8자 이상이며 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
}
