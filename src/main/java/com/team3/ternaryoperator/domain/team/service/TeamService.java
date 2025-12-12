package com.team3.ternaryoperator.domain.team.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.team.model.dto.MemberDto;
import com.team3.ternaryoperator.domain.team.model.dto.TeamDto;
import com.team3.ternaryoperator.domain.team.model.dto.TeamMemberDetailDto;
import com.team3.ternaryoperator.domain.team.model.dto.TeamMemberDto;
import com.team3.ternaryoperator.domain.team.model.request.TeamCreateMemberRequest;
import com.team3.ternaryoperator.domain.team.model.request.TeamRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamDetailResponse;
import com.team3.ternaryoperator.domain.team.model.response.TeamGetMemberResponse;
import com.team3.ternaryoperator.domain.team.model.response.TeamResponse;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.team3.ternaryoperator.common.exception.ErrorCode.TEAM_DUPLICATE_NAME;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamDetailResponse createTeam(AuthUser authUser, TeamRequest request) {

        // 인증된 사용자 정보가 유효하지 않을 경우 NOT_LOGGED_IN(401)로 응답
        User me = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String name = request.getName();
        String description = request.getDescription();

        // 팀 이름 중복 체크
        if (teamRepository.existsByName(name)) {
            throw new CustomException(TEAM_DUPLICATE_NAME);
        }

        Team newTeam = new Team(name, description);
        Team savedTeam = teamRepository.save(newTeam);

        me.changeTeam(savedTeam); // 소속 설정(team_id 세팅)
        userRepository.save(me);

        return toTeamResponse(savedTeam);
    }

    @Transactional(readOnly = true)
    public List<TeamDetailResponse> getAllTeam() {

        List<Team> teams = teamRepository.findAllByDeletedAtIsNull();

        return teams.stream()
                .map(this::toTeamResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamDetailResponse getOneTeam(Long id) {

        Team foundTeam = getTeamOrThrow(id);

        return toTeamResponse(foundTeam);
    }

    @Transactional
    public TeamDetailResponse updateTeam(AuthUser authUser, Long id, TeamRequest request) {

        Team foundTeam = getTeamOrThrow(id);

        // 수정 권한 확인
        boolean isMember = userRepository.existsByIdAndTeam_Id(authUser.getId(), id);
        if (!isMember) {
            throw new CustomException(ErrorCode.TEAM_UPDATE_PERMISSION_DENIED);
        }

        String name = request.getName();
        String description = request.getDescription();

        foundTeam.updateTeamInformation(name, description);

        return toTeamResponse(foundTeam);
    }

    @Transactional
    public void deleteTeam(AuthUser authUser, Long id) {

        Team foundTeam = getTeamOrThrow(id);

        // 삭제 권한 확인
        boolean isMember = userRepository.existsByIdAndTeam_Id(authUser.getId(), id);
        if (!isMember) {
            throw new CustomException(ErrorCode.TEAM_DELETE_PERMISSION_DENIED);
        }

        // 팀 멤버 수 확인
        long memberCount = userRepository.countByTeam_Id(id);
        if (memberCount > 1) {
            throw new CustomException(ErrorCode.TEAM_MEMBER_EXIST);
        }

        // 본인 팀 삭제
        User me = userRepository.getReferenceById(authUser.getId());
        me.changeTeam(null);

        foundTeam.softDelete();
    }

    @Transactional
    public TeamResponse createTeamMember(Long teamId, @Valid TeamCreateMemberRequest request) {

        Team foundTeam = getTeamOrThrow(teamId);

        // 추가할 멤버 조회
        User targetUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 소속 체크
        Team current = targetUser.getTeam();
        if (current != null) {
            throw new CustomException(ErrorCode.TEAM_ALREADY_MEMBER);
        }

        targetUser.joinTeam(foundTeam);
        userRepository.save(targetUser);

        List<TeamMemberDto> members = userRepository.findByTeamId(teamId).stream()
                .map(TeamMemberDto::from)
                .toList();

        return TeamResponse.fromMembers(TeamDto.from(foundTeam), members);
    }

    @Transactional(readOnly = true)
    public List<TeamGetMemberResponse> getTeamMember(Long teamId) {

        getTeamOrThrow(teamId);

        // 팀 멤버 조회
        List<TeamMemberDetailDto> members = userRepository.findByTeamId(teamId)
                .stream()
                .map(TeamMemberDetailDto::from)
                .toList();

        return members.stream()
                .map(TeamGetMemberResponse::from)
                .toList();
    }

    @Transactional
    public void deleteTeamMember(AuthUser authUser, Long teamId, Long userId) {

        getTeamOrThrow(teamId);

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유저가 이 팀 소속이 아닌 경우
        if (target.getTeam() == null || !target.getTeam().getId().equals(teamId)) {
            throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
        }

        // 제거 권한 확인
        if (!authUser.getId().equals(userId)) {
            throw new CustomException(ErrorCode.TEAM_MEMBER_DELETE_PERMISSION_DENIED);
        }

        target.changeTeam(null);
        userRepository.save(target);
    }

    // 헬퍼 메서드
    private TeamDetailResponse toTeamResponse(Team team) {
        List<User> users = userRepository.findByTeamId(team.getId());
        List<MemberDto> members = users.stream()
                .map(MemberDto::from)
                .toList();

        return TeamDetailResponse.fromDetail(TeamDto.from(team), members);
    }

    // 팀 조회 시 일치하는 팀이 없으면 TEAM_NOT_FOUND 예외 발생
    private Team getTeamOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
    }
}