package com.team3.ternaryoperator.domain.team.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.common.exception.TeamException;
import com.team3.ternaryoperator.common.exception.UserException;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // 팀 생성
    @Transactional
    public TeamDetailResponse createTeam(AuthUser authUser, TeamRequest request) {
        User me = getUserOrThrow(authUser.getId());

        if (teamRepository.existsByName(request.getName())) {
            throw new TeamException(ErrorCode.TEAM_DUPLICATE_NAME);
        }

        Team savedTeam = teamRepository.save(new Team(request.getName(), request.getDescription()));

        me.changeTeam(savedTeam);
        userRepository.save(me);

        return toTeamResponse(savedTeam);
    }

    // 팀 전체 조회
    @Transactional(readOnly = true)
    public List<TeamDetailResponse> getAllTeam() {
        return teamRepository.findAllByDeletedAtIsNull().stream()
                .map(this::toTeamResponse)
                .toList();
    }

    // 팀 단건 조회
    @Transactional(readOnly = true)
    public TeamDetailResponse getOneTeam(Long id) {
        Team foundTeam = getTeamOrThrow(id);
        return toTeamResponse(foundTeam);
    }

    // 팀 정보 수정
    @Transactional
    public TeamDetailResponse updateTeam(AuthUser authUser, Long id, TeamRequest request) {
        Team foundTeam = getTeamOrThrow(id);
        validateTeamMember(authUser.getId(), id);

        foundTeam.updateTeamInformation(request.getName(), request.getDescription());

        return toTeamResponse(foundTeam);
    }

    // 팀 삭제
    @Transactional
    public void deleteTeam(AuthUser authUser, Long id) {
        Team foundTeam = getTeamOrThrow(id);
        validateTeamMember(authUser.getId(), id);

        validateTeamHasSingleMember(id);

        User me = userRepository.getReferenceById(authUser.getId());
        me.changeTeam(null);

        foundTeam.softDelete();
    }

    // 팀 멤버 추가
    @Transactional
    public TeamResponse createTeamMember(Long teamId, TeamCreateMemberRequest request) {
        Team foundTeam = getTeamOrThrow(teamId);
        User targetUser = getUserOrThrow(request.getUserId());

        validateNotInAnyTeam(targetUser);

        targetUser.joinTeam(foundTeam);
        userRepository.save(targetUser);

        List<TeamMemberDto> members = userRepository.findByTeamId(teamId).stream()
                .map(TeamMemberDto::from)
                .toList();

        return TeamResponse.fromMembers(TeamDto.from(foundTeam), members);
    }

    // 팀 멤버 조회
    @Transactional(readOnly = true)
    public List<TeamGetMemberResponse> getTeamMember(Long teamId) {
        getTeamOrThrow(teamId);

        List<TeamMemberDetailDto> members = userRepository.findByTeamId(teamId).stream()
                .map(TeamMemberDetailDto::from)
                .toList();

        return members.stream()
                .map(TeamGetMemberResponse::from)
                .toList();
    }

    // 팀 멤버 삭제
    @Transactional
    public void deleteTeamMember(AuthUser authUser, Long teamId, Long userId) {
        getTeamOrThrow(teamId);

        User target = getUserOrThrow(userId);
        validateBelongsToTeam(target, teamId);
        validateSelfAction(authUser.getId(), userId);

        target.changeTeam(null);
        userRepository.save(target);
    }

    // Team 응답 변환
    private TeamDetailResponse toTeamResponse(Team team) {
        List<MemberDto> members = userRepository.findByTeamId(team.getId()).stream()
                .map(MemberDto::from)
                .toList();

        return TeamDetailResponse.fromDetail(TeamDto.from(team), members);
    }

    // Team 찾기 (없으면 예외 발생)
    private Team getTeamOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorCode.TEAM_NOT_FOUND));
    }

    // User 찾기 (없으면 예외 발생)
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    // Team 멤버 권한 확인 (없으면 예외 발생)
    private void validateTeamMember(Long userId, Long teamId) {
        boolean isMember = userRepository.existsByIdAndTeam_Id(userId, teamId);
        if (!isMember) {
            throw new TeamException(ErrorCode.TEAM_MEMBER_EXIST);
        }
    }

    // Team 멤버 수가 1명인지 확인 (아니면 예외 발생)
    private void validateTeamHasSingleMember(Long teamId) {
        long memberCount = userRepository.countByTeam_Id(teamId);
        if (memberCount > 1) {
            throw new TeamException(ErrorCode.TEAM_MEMBER_EXIST);
        }
    }

    // 유저가 이미 Team 소속이면 예외 발생
    private void validateNotInAnyTeam(User user) {
        if (user.getTeam() != null) {
            throw new TeamException(ErrorCode.TEAM_ALREADY_MEMBER);
        }
    }

    // 유저가 해당 Team 소속이 아니면 예외 발생
    private void validateBelongsToTeam(User user, Long teamId) {
        if (user.getTeam() == null || !user.getTeam().getId().equals(teamId)) {
            throw new TeamException(ErrorCode.TEAM_NOT_FOUND);
        }
    }

    // 본인 행위가 아니면 예외 발생
    private void validateSelfAction(Long authUserId, Long userId) {
        if (!authUserId.equals(userId)) {
            throw new TeamException(ErrorCode.TEAM_MEMBER_DELETE_PERMISSION_DENIED);
        }
    }
}
