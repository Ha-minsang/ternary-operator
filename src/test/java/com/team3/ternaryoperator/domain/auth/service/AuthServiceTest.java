package com.team3.ternaryoperator.domain.auth.service;

import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.common.security.JwtUtil;
import com.team3.ternaryoperator.domain.auth.dto.request.LoginRequest;
import com.team3.ternaryoperator.domain.auth.dto.request.VerifyPasswordRequest;
import com.team3.ternaryoperator.domain.auth.dto.response.LoginResponse;
import com.team3.ternaryoperator.domain.auth.dto.response.VerifyPasswordResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("로그인 성공")
    void login_ShouldReturnToken() {
        // given
        LoginRequest request = new LoginRequest("testUsername", "testPassword");

        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .willReturn(true);
        given(jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getRole()))
                .willReturn("token");

        // when
        LoginResponse response = authService.login(request);

        // then
        assertNotNull(response);
        assertEquals("token", response.getToken());
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void login_ShouldThrowException_WhenUserNotFound() {
        // given
        LoginRequest request = new LoginRequest("testUsername", "testPassword");

        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.login(request)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_ShouldThrowException_WhenInvalidPassword() {
        // given
        LoginRequest request = new LoginRequest("testUsername", "wrongPassword");

        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.login(request)
        );

        // then
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    @DisplayName("비밀번호 확인 성공")
    void checkPassword_ShouldReturnTrue() {
        // given
        Long userId = 1L;
        VerifyPasswordRequest request = new VerifyPasswordRequest("testPassword");

        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .willReturn(true);

        // when
        VerifyPasswordResponse response = authService.checkPassword(userId, request);

        // then
        assertNotNull(response);
        assertTrue(response.isValid());
    }

    @Test
    @DisplayName("비밀번호 확인 실패 - 사용자 없음")
    void checkPassword_ShouldThrowException_WhenUserNotFound() {
        // given
        Long userId = 1L;
        VerifyPasswordRequest request = new VerifyPasswordRequest("testPassword");

        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.checkPassword(userId, request)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("비밀번호 확인 실패 - 비밀번호 불일치")
    void checkPassword_ShouldThrowException_WhenInvalidPassword() {
        // given
        Long userId = 1L;
        VerifyPasswordRequest request = new VerifyPasswordRequest("wrongPassword");

        User user = UserFixture.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .willReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.checkPassword(userId, request)
        );

        // then
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
