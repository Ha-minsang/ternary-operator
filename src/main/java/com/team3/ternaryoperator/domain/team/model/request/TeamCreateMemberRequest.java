package com.team3.ternaryoperator.domain.team.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCreateMemberRequest {

    @NotNull
    private Long userId;
}