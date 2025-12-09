package com.team3.ternaryoperator.team.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.team.model.request.TeamCreateRequest;
import com.team3.ternaryoperator.team.model.response.TeamCreateResponse;
import com.team3.ternaryoperator.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<CommonResponse<TeamCreateResponse>> createTeamApi(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody TeamCreateRequest request) {

        TeamCreateResponse response = teamService.createTeam(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(response, "팀이 생성되었습니다."));
    }
}