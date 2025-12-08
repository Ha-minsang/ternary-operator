package com.team3.ternaryoperator.domain.auth.service;

import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.common.util.JwtUtil;
import com.team3.ternaryoperator.domain.auth.dto.request.LoginRequest;
import com.team3.ternaryoperator.domain.auth.dto.response.LoginResponse;
import com.team3.ternaryoperator.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches) {
            throw new CustomException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        String token = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getRole());

        return new LoginResponse(token);
    }
}
