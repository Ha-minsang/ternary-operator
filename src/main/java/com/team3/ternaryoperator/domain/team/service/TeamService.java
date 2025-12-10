package com.team3.ternaryoperator.domain.team.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.team.model.dto.MemberDto;
import com.team3.ternaryoperator.domain.team.model.dto.TeamDto;
import com.team3.ternaryoperator.domain.team.model.dto.TeamMemberDto;
import com.team3.ternaryoperator.domain.team.model.request.TeamCreateMemberRequest;
import com.team3.ternaryoperator.domain.team.model.request.TeamRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamDetailResponse;
import com.team3.ternaryoperator.domain.team.model.response.TeamResponse;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.team3.ternaryoperator.common.exception.ErrorCode.TEAM_NAME_DUPLICATED;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Transactional
    public TeamDetailResponse createTeam(AuthUser authUser, TeamRequest request) {

        // 인증된 사용자 정보가 유효하지 않을 경우 NOT_LOGGED_IN(401)로 응답
        User me = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_LOGGED_IN));

        String name = request.getName();
        String description = request.getDescription();

        // 팀 이름 중복 체크
        if (teamRepository.existsByName(name)) {
            throw new CustomException(TEAM_NAME_DUPLICATED);
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

        Team foundTeam = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        return toTeamResponse(foundTeam);
    }

    @Transactional
    public TeamDetailResponse updateTeam(AuthUser authUser, Long id, TeamRequest request) {

        Team foundTeam = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        boolean isMember = userRepository.existsByIdAndTeamId(authUser.getId(), id);
        if (!isMember) {
            throw new CustomException(ErrorCode.NO_PERMISSION_TEAM_UPDATE);
        }

        String name = request.getName();
        String description = request.getDescription();

        foundTeam.updateTeamInformation(name, description);

        return toTeamResponse(foundTeam);
    }

    @Transactional
    public void deleteTeam(AuthUser authUser, Long id) {

        Team foundTeam = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 삭제 권한 확인
        boolean isMember = userRepository.existsByIdAndTeamId(authUser.getId(), id);
        if (!isMember) {
            throw new CustomException(ErrorCode.NO_PERMISSION_TEAM_DELETE);
        }

        // 팀 멤버 수 확인
        long memberCount = userRepository.countByTeamId(id);
        if (memberCount > 1) {
            throw new CustomException(ErrorCode.EXIST_MEMBER);
        }

        // 본인 팀 삭제
        User me = userRepository.getReferenceById(authUser.getId());
        me.changeTeam(null);

        foundTeam.softDelete();
    }

    @Transactional
    public TeamResponse createTeamMember(Long teamId, @Valid TeamCreateMemberRequest request) {

        // 팀 조회
        Team foundTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        // 추가할 멤버 조회
        User targetUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 소속 체크
        Team current = targetUser.getTeam();
        if (current != null) {
            throw new CustomException(ErrorCode.ALREADY_IN_TEAM);
        }

        targetUser.joinTeam(foundTeam);
        userRepository.save(targetUser);

        List<TeamMemberDto> members = userRepository.findByTeamId(teamId).stream()
                .map(TeamMemberDto::from)
                .toList();

        return TeamResponse.fromMembers(TeamDto.from(foundTeam), members);
    }

    // 헬퍼 메서드
    private TeamDetailResponse toTeamResponse(Team team) {
        List<User> users = userRepository.findByTeamId(team.getId());
        List<MemberDto> members = users.stream()
                .map(MemberDto::from)
                .toList();

        return TeamDetailResponse.fromDetail(TeamDto.from(team), members);
    }
}