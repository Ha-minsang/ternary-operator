package com.team3.ternaryoperator.domain.team.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateMemberRequest {

    @NotNull
    private Long userId;
}