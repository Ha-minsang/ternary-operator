package com.team3.ternaryoperator.domain.team.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.team.model.request.TeamCreateRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamCreateResponse;
import com.team3.ternaryoperator.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<CommonResponse<TeamCreateResponse>> createTeamApi(@Valid @RequestBody TeamCreateRequest request) {

        TeamCreateResponse response = teamService.createTeam(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(response, "팀이 생성되었습니다."));
    }
}