package com.team3.ternaryoperator.domain.team.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TeamRequest {

    @NotBlank(message = "팀 이름은 필수입니다.")
    @Size(max = 30)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String description;
}