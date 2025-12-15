package com.team3.ternaryoperator.domain.auth.service;

import com.team3.ternaryoperator.common.aspect.ActivityLog;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.AuthException;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.common.exception.UserException;
import com.team3.ternaryoperator.common.security.JwtUtil;
import com.team3.ternaryoperator.domain.auth.dto.request.LoginRequest;
import com.team3.ternaryoperator.domain.auth.dto.request.VerifyPasswordRequest;
import com.team3.ternaryoperator.domain.auth.dto.response.LoginResponse;
import com.team3.ternaryoperator.domain.auth.dto.response.VerifyPasswordResponse;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    @ActivityLog
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = getUserByUsernameOrThrow(request.getUsername());

        validatePassword(request.getPassword(), user.getPassword());

        String token = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getRole());

        return new LoginResponse(token);
    }

    // 비밀번호 일치 확인
    @Transactional
    public VerifyPasswordResponse checkPassword(Long userId, VerifyPasswordRequest request) {
        User user = getUserByIdOrThrow(userId);

        validatePassword(request.getPassword(), user.getPassword());

        return new VerifyPasswordResponse(true);
    }

    // User 찾기 (없으면 예외 발생)
    private User getUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    // User 찾기 (없으면 예외 발생)
    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    // 비밀번호 불일치시 예외 발생
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
