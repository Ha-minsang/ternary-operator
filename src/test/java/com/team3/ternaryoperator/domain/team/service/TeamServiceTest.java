package com.team3.ternaryoperator.domain.team.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.team.model.request.TeamRequest;
import com.team3.ternaryoperator.domain.team.model.response.TeamDetailResponse;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void CreateTeam_success() {

        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();

        TeamRequest teamRequest = new TeamRequest("삼항연산자 팀", "백엔드 팀");

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
        assertThat(response.getName()).isEqualTo("testTeam");
        assertThat(response.getDescription()).isEqualTo("test team description");
    }

    @Test
    @DisplayName("팀 생성 실패 - 사용자 정보가 유효하지 않을 때")
    void CreateTeam_fail_1번_케이스() {

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
    void CreateTeam_fail_2번_케이스() {

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
        assertEquals(ErrorCode.TEAM_NAME_DUPLICATED, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 전체 조회 성공")
    void GetAllTeam_success() {

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
    void GetOneTeam_success() {

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
    void GetOneTeam_fail() {

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
    void UpdateTeam_success() {

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
    void UpdateTeam_fail_1번_케이스() {

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
    void UpdateTeam_fail_2번_케이스() {

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
        assertEquals(ErrorCode.NO_PERMISSION_TEAM_UPDATE, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 삭제 성공")
    void DeleteTeam_success() {

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
    void DeleteTeam_fail_1번_케이스() {

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
    void DeleteTeam_fail_2번_케이스() {

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
        assertEquals(ErrorCode.NO_PERMISSION_TEAM_DELETE, exception.getErrorCode());
    }

    @Test
    @DisplayName("팀 삭제 실패 - 팀원 2명 이상")
    void DeleteTeam_fail_3번_케이스() {

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
        assertEquals(ErrorCode.EXIST_MEMBER, exception.getErrorCode());
    }
}