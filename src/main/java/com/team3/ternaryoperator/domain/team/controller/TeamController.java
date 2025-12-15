package com.team3.ternaryoperator.domain.team.controller;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.CommonResponse;
import com.team3.ternaryoperator.domain.team.model.request.TeamCreateMemberRequest;
import com.team3.ternaryoperator.domain.team.model.request.TeamRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamDetailResponse;
import com.team3.ternaryoperator.domain.team.model.response.TeamGetMemberResponse;
import com.team3.ternaryoperator.domain.team.model.response.TeamResponse;
import com.team3.ternaryoperator.domain.team.service.TeamService;
import jakarta.validation.Valid;
import java.util.List;
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
    public ResponseEntity<CommonResponse<TeamDetailResponse>> createTeam(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody TeamRequest request
    ) {
        TeamDetailResponse response = teamService.createTeam(authUser, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(response, "팀이 생성되었습니다."));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TeamDetailResponse>>> getAllTeam(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<TeamDetailResponse> response = teamService.getAllTeam();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "팀 목록 조회 성공."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TeamDetailResponse>> getOneTeam(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id
    ) {
        TeamDetailResponse response = teamService.getOneTeam(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "팀 조회 성공."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<TeamDetailResponse>> updateTeam(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @Valid @RequestBody TeamRequest request
    ) {
        TeamDetailResponse response = teamService.updateTeam(authUser, id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "팀 정보가 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteTeam(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id
    ) {
        teamService.deleteTeam(authUser, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(null, "팀이 삭제되었습니다."));
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<CommonResponse<TeamResponse>> createTeamMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamCreateMemberRequest request
    ) {
        TeamResponse response = teamService.createTeamMember(teamId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "팀 멤버가 추가되었습니다."));
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<CommonResponse<List<TeamGetMemberResponse>>> getTeamMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long teamId
    ) {
        List<TeamGetMemberResponse> response = teamService.getTeamMember(teamId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(response, "팀 멤버 조회 성공."));
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<CommonResponse<Void>> deleteTeamMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        teamService.deleteTeamMember(authUser, teamId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(null, "팀 멤버가 제거되었습니다."));
    }
}