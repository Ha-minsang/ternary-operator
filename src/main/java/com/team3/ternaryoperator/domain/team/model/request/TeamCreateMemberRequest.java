package com.team3.ternaryoperator.domain.team.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateMemberRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private Long userId;
}