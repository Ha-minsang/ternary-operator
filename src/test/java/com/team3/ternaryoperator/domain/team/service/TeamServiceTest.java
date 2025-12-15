package com.team3.ternaryoperator.domain.team.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.team.model.dto.TeamMemberDto;
import com.team3.ternaryoperator.domain.team.model.request.TeamCreateMemberRequest;
import com.team3.ternaryoperator.domain.team.model.request.TeamRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamDetailResponse;
import com.team3.ternaryoperator.domain.team.model.response.TeamGetMemberResponse;
import com.team3.ternaryoperator.domain.team.model.response.TeamResponse;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import com.team3.ternaryoperator.support.AuthUserFixture;
import com.team3.ternaryoperator.support.TeamFixture;
import com.team3.ternaryoperator.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    @DisplayName("팀 생성 성공")
    void createTeam_success() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        TeamRequest teamRequest = new TeamRequest("testTeam", "test team description");

        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        Team savedteam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(savedteam, "id", 100L);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(teamRepository.existsByName(teamRequest.getName())).willReturn(false);
        given(teamRepository.save(any(Team.class))).willReturn(savedteam);

        // when
        TeamDetailResponse response = teamService.createTeam(authUser, teamRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getName()).isEqualTo(savedteam.getName());
        assertThat(response.getDescription()).isEqualTo(savedteam.getDescription());
    }

    @Test
    @DisplayName("팀 생성 실패 - 사용자 정보가 유효하지 않을 때")
    void createTeam_fail_1번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        TeamRequest teamRequest = new TeamRequest("삼항연산자 팀", "백엔드 팀");

        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.createTeam(authUser, teamRequest));

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 생성 실패 - 팀 이름 중복")
    void createTeam_fail_2번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        TeamRequest teamRequest = new TeamRequest("삼항연산자 팀", "백엔드 팀");

        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        Team savedteam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(savedteam, "id", 100L);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(teamRepository.existsByName(teamRequest.getName())).willReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.createTeam(authUser, teamRequest));

        // then
        assertEquals(ErrorCode.TEAM_DUPLICATE_NAME, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 전체 조회 성공")
    void getAllTeam_success() {

        // given
        Team Ateam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(Ateam, "id", 1L);
        Team Bteam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(Bteam, "id", 2L);
        Team Cteam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(Cteam, "id", 3L);

        given(teamRepository.findAllByDeletedAtIsNull()).willReturn(List.of(Ateam, Bteam, Cteam));

        // when
        List<TeamDetailResponse> response = teamService.getAllTeam();

        // then
        assertEquals(3, response.size());
        assertNotNull(response.get(0).getId());
        assertNotNull(response.get(0).getName());
        assertNotNull(response.get(0).getDescription());
    }

    @Test
    @DisplayName("팀 단건 조회 성공")
    void getOneTeam_success() {

        // given
        Team Ateam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(Ateam, "id", 1L);
        Team Bteam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(Bteam, "id", 2L);
        Team Cteam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(Cteam, "id", 3L);

        given(teamRepository.findById(2L)).willReturn(Optional.of(Bteam));

        // when
        TeamDetailResponse response = teamService.getOneTeam(2L);

        // then
        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getName()).isEqualTo("testTeam");
        assertThat(response.getDescription()).isEqualTo("test team description");
    }

    @Test
    @DisplayName("팀 단건 조회 실패 - 팀 없음")
    void getOneTeam_fail() {

        // given
        given(teamRepository.findById(99L)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.getOneTeam(99L));

        // then
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 수정 성공")
    void updateTeam_success() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long id = 100L;

        TeamRequest teamRequest = new TeamRequest("새 팀명", "새 팀 설명");

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", id);

        given(teamRepository.findById(id)).willReturn(Optional.of(foundTeam));
        given(userRepository.existsByIdAndTeam_Id(authUser.getId(), id)).willReturn(true);

        // when
        TeamDetailResponse response = teamService.updateTeam(authUser, id, teamRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(teamRequest.getName());
        assertThat(response.getDescription()).isEqualTo(teamRequest.getDescription());
    }

    @Test
    @DisplayName("팀 수정 실패 - 팀 없음")
    void updateTeam_fail_1번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long id = 100L;

        TeamRequest teamRequest = new TeamRequest("새 팀명", "새 팀 설명");
        given(teamRepository.findById(id)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.updateTeam(authUser, id, teamRequest));

        // then
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 수정 실패 - 팀 수정 권한 없음")
    void updateTeam_fail_2번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long id = 100L;

        TeamRequest teamRequest = new TeamRequest("새 팀명", "새 팀 설명");

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", id);

        given(teamRepository.findById(id)).willReturn(Optional.of(foundTeam));
        given(userRepository.existsByIdAndTeam_Id(authUser.getId(), id)).willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.updateTeam(authUser, id, teamRequest));

        // then
        assertEquals(ErrorCode.TEAM_UPDATE_PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 삭제 성공")
    void deleteTeam_success() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long id = 100L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", id);

        User me = UserFixture.createUser();
        ReflectionTestUtils.setField(me, "id", authUser.getId());

        given(teamRepository.findById(id)).willReturn(Optional.of(foundTeam));
        given(userRepository.existsByIdAndTeam_Id(authUser.getId(), id)).willReturn(true);
        given(userRepository.countByTeam_Id(id)).willReturn(1L);
        given(userRepository.getReferenceById(authUser.getId())).willReturn(me);

        // when
        teamService.deleteTeam(authUser, id);

        // then
        assertThat(foundTeam.getDeletedAt()).isNotNull();
        assertThat(me.getTeam()).isNull();
    }

    @Test
    @DisplayName("팀 삭제 실패 - 팀 없음")
    void deleteTeam_fail_1번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long id = 100L;

        given(teamRepository.findById(id)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.deleteTeam(authUser, id));

        // then
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 삭제 실패 - 팀 삭제 권한 없음")
    void deleteTeam_fail_2번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long id = 100L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", id);

        given(teamRepository.findById(id)).willReturn(Optional.of(foundTeam));
        given(userRepository.existsByIdAndTeam_Id(authUser.getId(), id)).willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.deleteTeam(authUser, id));

        // then
        assertEquals(ErrorCode.TEAM_DELETE_PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 삭제 실패 - 팀원 2명 이상")
    void deleteTeam_fail_3번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long id = 100L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", id);

        given(teamRepository.findById(id)).willReturn(Optional.of(foundTeam));
        given(userRepository.existsByIdAndTeam_Id(authUser.getId(), id)).willReturn(true);
        given(userRepository.countByTeam_Id(id)).willReturn(2L);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.deleteTeam(authUser, id));

        // then
        assertEquals(ErrorCode.TEAM_MEMBER_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 추가 성공")
    void createTeamMember_success() {

        // given
        Long teamId = 100L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", teamId);

        TeamCreateMemberRequest teamCreateMemberRequest = new TeamCreateMemberRequest(1L);

        User targetUser = UserFixture.createUser();
        ReflectionTestUtils.setField(targetUser, "id", teamCreateMemberRequest.getUserId());

        assertThat(targetUser.getTeam()).isNull();

        given(teamRepository.findById(teamId)).willReturn(Optional.of(foundTeam));
        given(userRepository.findById(teamCreateMemberRequest.getUserId())).willReturn(Optional.of(targetUser));
        given(userRepository.findByTeamId(teamId)).willReturn(List.of(targetUser));
        given(userRepository.save(any(User.class))).willReturn(targetUser);

        // when
        TeamResponse response = teamService.createTeamMember(teamId, teamCreateMemberRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(teamId);
        assertThat(response.getMembers()).hasSize(1);

        TeamMemberDto teamMemberDto = response.getMembers().get(0);
        assertThat(teamMemberDto.getId()).isEqualTo(teamCreateMemberRequest.getUserId());
        assertThat(teamMemberDto.getUsername()).isEqualTo(targetUser.getUsername());
        assertThat(teamMemberDto.getName()).isEqualTo(targetUser.getName());
    }

    @Test
    @DisplayName("팀 멤버 추가 실패 - 팀 없음")
    void createTeamMember_fail_1번_케이스() {

        // given
        Long teamId = 100L;
        TeamCreateMemberRequest teamCreateMemberRequest = new TeamCreateMemberRequest(1L);

        given(teamRepository.findById(teamId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.createTeamMember(teamId, teamCreateMemberRequest));

        // then
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 추가 실패 - 추가할 멤버 조회 실패")
    void createTeamMember_fail_2번_케이스() {

        // given
        Long teamId = 100L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", teamId);

        TeamCreateMemberRequest teamCreateMemberRequest = new TeamCreateMemberRequest(1L);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(foundTeam));
        given(userRepository.findById(teamCreateMemberRequest.getUserId())).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.createTeamMember(teamId, teamCreateMemberRequest));

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 추가 실패- 이미 팀이 있는 경우")
    void createTeamMember_3번_케이스() {

        // given
        Long teamId = 100L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", teamId);

        TeamCreateMemberRequest teamCreateMemberRequest = new TeamCreateMemberRequest(1L);

        User targetUser = UserFixture.createUser();
        ReflectionTestUtils.setField(targetUser, "id", teamCreateMemberRequest.getUserId());

        Team otherTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(otherTeam, "id", 999L);
        ReflectionTestUtils.setField(targetUser, "team", otherTeam);

        assertThat(targetUser.getTeam()).isNotNull();

        given(teamRepository.findById(teamId)).willReturn(Optional.of(foundTeam));
        given(userRepository.findById(teamCreateMemberRequest.getUserId())).willReturn(Optional.of(targetUser));

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.createTeamMember(teamId, teamCreateMemberRequest));

        // then
        assertEquals(ErrorCode.TEAM_ALREADY_MEMBER, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 조회 성공")
    void getTeamMember_success() {

        // given
        Long teamId = 100L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", teamId);

        User user1 = UserFixture.createUser();
        ReflectionTestUtils.setField(user1, "id", 1L);
        ReflectionTestUtils.setField(user1, "team", foundTeam);

        User user2 = UserFixture.createUser();
        ReflectionTestUtils.setField(user2, "id", 2L);
        ReflectionTestUtils.setField(user2, "team", foundTeam);

        List<User> users = List.of(user1, user2);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(foundTeam));
        given(userRepository.findByTeamId(teamId)).willReturn(users);

        // when
        List<TeamGetMemberResponse> response = teamService.getTeamMember(teamId);

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getId()).isEqualTo(1L);
        assertThat(response.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("팀 멤버 조회 실패 - 팀 없음")
    void getTeamMember_fail() {

        // given
        Long teamId = 100L;

        given(teamRepository.findById(teamId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.getTeamMember(teamId));

        // then
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 삭제 성공")
    void deleteTeamMember_success() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long teamId = 100L;

        Long userId = 1L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", teamId);

        User target = UserFixture.createUser();
        ReflectionTestUtils.setField(target, "id", userId);
        ReflectionTestUtils.setField(target, "team", foundTeam);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(foundTeam));
        given(userRepository.findById(userId)).willReturn(Optional.of(target));
        given(userRepository.save(any(User.class))).willReturn(target);

        // when
        teamService.deleteTeamMember(authUser, teamId, userId);

        // then
        assertThat(target.getTeam()).isNull();
    }

    @Test
    @DisplayName("팀 멤버 삭제 실패 - 팀 없음")
    void deleteTeamMember_fail_1번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long teamId = 100L;

        Long userId = 1L;

        given(teamRepository.findById(teamId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.deleteTeamMember(authUser, teamId, userId));

        // then
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 삭제 실패 - 유저 없음")
    void deleteTeamMember_fail_2번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long teamId = 100L;

        Long userId = 1L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", teamId);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(foundTeam));
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.deleteTeamMember(authUser, teamId, userId));

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 삭제 실패 - 다른 팀 소속")
    void deleteTeamMember_fail_3번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long teamId = 100L;

        Long userId = 1L;

        Team requestTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(requestTeam, "id", teamId);

        Team otherTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(otherTeam, "id", 999L);

        User target = UserFixture.createUser();
        ReflectionTestUtils.setField(target, "id", userId);
        ReflectionTestUtils.setField(target, "team", otherTeam);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(requestTeam));
        given(userRepository.findById(userId)).willReturn(Optional.of(target));

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.deleteTeamMember(authUser, teamId, userId));

        // then
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 멤버 삭제 실패 - 제거 권한 없음")
    void deleteTeamMember_fail_4번_케이스() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        Long teamId = 100L;

        Long userId = 2L;

        Team foundTeam = TeamFixture.createTeam();
        ReflectionTestUtils.setField(foundTeam, "id", teamId);

        User target = UserFixture.createUser();
        ReflectionTestUtils.setField(target, "id", userId);
        ReflectionTestUtils.setField(target, "team", foundTeam);

        given(teamRepository.findById(teamId)).willReturn(Optional.of(foundTeam));
        given(userRepository.findById(userId)).willReturn(Optional.of(target));

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> teamService.deleteTeamMember(authUser, teamId, userId));

        // then
        assertEquals(ErrorCode.TEAM_MEMBER_DELETE_PERMISSION_DENIED, exception.getErrorCode());
    }
}