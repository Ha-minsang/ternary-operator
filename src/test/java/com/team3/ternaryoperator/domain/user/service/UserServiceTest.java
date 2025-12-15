package com.team3.ternaryoperator.domain.user.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.model.request.UserCreateRequest;
import com.team3.ternaryoperator.domain.user.model.request.UserUpdateRequest;
import com.team3.ternaryoperator.domain.user.model.response.UserDetailResponse;
import com.team3.ternaryoperator.domain.user.model.response.UserResponse;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import com.team3.ternaryoperator.support.AuthUserFixture;
import com.team3.ternaryoperator.support.TaskFixture;
import com.team3.ternaryoperator.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_ShouldCreateUser() {
        // given
        UserCreateRequest request = new UserCreateRequest(
                "testUsername",
                "test@email.com",
                "testPassword",
                "testName"
        );
        String encodedPassword = "encodedPassword";

        given(userRepository.existsByUsername(request.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn(encodedPassword);

        User savedUser = UserFixture.createUser();
        ReflectionTestUtils.setField(savedUser, "id", 1L);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        UserResponse response = userService.signUp(request);

        // then
        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getId());
        assertEquals(savedUser.getUsername(), response.getUsername());
        assertEquals(savedUser.getEmail(), response.getEmail());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 username")
    void signUp_ShouldThrowException_WhenUsernameDuplicated() {
        // given
        UserCreateRequest request = new UserCreateRequest(
                "testUsername",
                "test@email.com",
                "testPassword",
                "testName"
        );

        given(userRepository.existsByUsername(request.getUsername())).willReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.signUp(request)
        );

        // then
        assertEquals(ErrorCode.USER_DUPLICATE_USERNAME, exception.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 email")
    void signUp_ShouldThrowException_WhenEmailDuplicated() {
        // given
        UserCreateRequest request = new UserCreateRequest(
                "testUsername",
                "test@email.com",
                "testPassword",
                "testName"
        );

        given(userRepository.existsByUsername(request.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.signUp(request)
        );

        // then
        assertEquals(ErrorCode.USER_DUPLICATE_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("단건 사용자 조회 성공")
    void getUser_ShouldReturnUserDetail() {
        // given
        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        UserDetailResponse response = userService.getOneUser(1L);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    @DisplayName("단건 사용자 조회 실패 - 사용자 없음")
    void getUser_ShouldThrowException_WhenUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.getOneUser(1L)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("전체 사용자 조회 성공")
    void getUsers_ShouldReturnUsers() {
        // given
        User user1 = UserFixture.createUser();
        User user2 = UserFixture.createUser();
        ReflectionTestUtils.setField(user1, "id", 1L);
        ReflectionTestUtils.setField(user2, "id", 2L);

        given(userRepository.findAll()).willReturn(List.of(user1, user2));

        // when
        List<UserResponse> responses = userService.getUsers();

        // then
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(2L, responses.get(1).getId());
    }

    @Test
    @DisplayName("사용자 정보 수정 성공")
    void updateUser_ShouldUpdateUser() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        UserUpdateRequest request = new UserUpdateRequest(
                "new@email.com",
                "newName",
                "testPassword"
        );

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(true);
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);

        // when
        UserDetailResponse response = userService.updateUser(authUser, authUser.getId(), request);

        // then
        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getName(), response.getName());
    }

    @Test
    @DisplayName("사용자 정보 수정 실패 - 비밀번호 불일치")
    void updateUser_ShouldThrowException_WhenInvalidPassword() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        UserUpdateRequest request = new UserUpdateRequest(
                "new@email.com",
                "newName",
                "wrongPassword"
        );

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.updateUser(authUser, authUser.getId(), request)
        );

        // then
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUser_ShouldSoftDeleteUserAndTasks() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", authUser.getId());

        Task task1 = TaskFixture.createTask(user);
        Task task2 = TaskFixture.createTask(user);

        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(taskRepository.findAllByAssigneeId(authUser.getId()))
                .willReturn(List.of(task1, task2));

        // when & then
        assertDoesNotThrow(() -> userService.deleteUser(authUser, authUser.getId()));
    }

    @Test
    @DisplayName("팀에 추가 가능한 사용자 조회 성공")
    void getAvailableUsers_ShouldReturnUsers() {
        // given
        User user1 = UserFixture.createUser();
        User user2 = UserFixture.createUser();
        ReflectionTestUtils.setField(user1, "id", 1L);
        ReflectionTestUtils.setField(user2, "id", 2L);

        given(userRepository.findAllByTeamIsNull()).willReturn(List.of(user1, user2));

        // when
        List<UserResponse> responses = userService.getAvailableUsers();

        // then
        assertEquals(2, responses.size());
    }
}