package com.team3.ternaryoperator.domain.team.controller;

import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.team.model.request.TeamCreateRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamResponse;
import com.team3.ternaryoperator.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<CommonResponse<TeamResponse>> createTeamApi(@Valid @RequestBody TeamCreateRequest request) {

        TeamResponse response = teamService.createTeam(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(response, "팀이 생성되었습니다."));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TeamResponse>>> getAllTeamApi() {

        List<TeamResponse> response = teamService.getAllTeam();

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response, "팀 목록 조회 성공"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TeamResponse>> getOneTeamApi(@PathVariable Long id) {

        TeamResponse response = teamService.getOneTeam(id);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response, "팀 조회 성공"));
    }
}