package com.team3.ternaryoperator.domain.team.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamRequest {

    @NotBlank(message = "팀 이름을 입력해주세요.")
    @Size(max = 30)
    private String name;

    @NotBlank(message = "설명을 입력해주세요.")
    @Size(max = 255)
    private String description;
}