package com.team3.ternaryoperator.domain.team.service;

import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.team.model.dto.MemberDto;
import com.team3.ternaryoperator.domain.team.model.dto.TeamDto;
import com.team3.ternaryoperator.domain.team.model.request.TeamCreateRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamResponse;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
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
    public TeamResponse createTeam(TeamCreateRequest request) {

        // user_not_found 검사는 하지 않음(보안 레이어에서 보장)

        String name = request.getName();
        String description = request.getDescription();

        // 팀 이름 중복 체크
        if (teamRepository.existsByName(name)) {
            throw new CustomException(TEAM_NAME_DUPLICATED);
        }

        Team newTeam = new Team(name, description);
        Team savedTeam = teamRepository.save(newTeam);

        return toTeamResponse(savedTeam);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeam() {

        List<Team> teams = teamRepository.findAllByDeletedAtIsNull();

        return teams.stream()
                .map(this::toTeamResponse)
                .toList();
        }

    @Transactional(readOnly = true)
    public TeamResponse getOneTeam(Long id) {

       Team foundTeam = teamRepository.findById(id)
               .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

       return toTeamResponse(foundTeam);
    }

    // 헬퍼 메서드
    private TeamResponse toTeamResponse(Team team) {
        List<MemberDto> members = userRepository.findByTeamId(team.getId())
                .stream()
                .map(MemberDto::from)
                .toList();

        return TeamResponse.from(TeamDto.from(team), members);
    }


}