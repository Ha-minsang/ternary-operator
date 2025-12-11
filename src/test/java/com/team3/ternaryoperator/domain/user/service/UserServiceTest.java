package com.team3.ternaryoperator.domain.user.service;

import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.model.request.UserCreateRequest;
import com.team3.ternaryoperator.domain.user.model.response.UserResponse;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import com.team3.ternaryoperator.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

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
        assertEquals(ErrorCode.DUPLICATE_USERNAME, exception.getErrorCode());
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
        assertEquals(ErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
    }
}