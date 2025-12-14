package com.team3.ternaryoperator.domain.user.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.AuthException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.common.exception.UserException;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.enums.UserRole;
import com.team3.ternaryoperator.domain.user.model.dto.UserDto;
import com.team3.ternaryoperator.domain.user.model.request.UserCreateRequest;
import com.team3.ternaryoperator.domain.user.model.request.UserUpdateRequest;
import com.team3.ternaryoperator.domain.user.model.response.UserDetailResponse;
import com.team3.ternaryoperator.domain.user.model.response.UserResponse;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public UserResponse signUp(UserCreateRequest request) {
        checkDuplicateUsername(request.getUsername());
        checkDuplicateEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), request.getEmail(), request.getName(), encodedPassword, UserRole.USER, null);
        User saved = userRepository.save(user);

        return UserResponse.from(UserDto.from(saved));
    }

    // User 단건 조회
    @Transactional(readOnly = true)
    public UserDetailResponse getOneUser(Long id) {
        User user = getUserByIdOrThrow(id);
        return UserDetailResponse.from(UserDto.from(user));
    }

    // 모든 User 조회
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .map(UserResponse::from)
                .toList();
    }

    // User 정보 수정
    @Transactional
    public UserDetailResponse updateUser(AuthUser authUser, Long userId, @Valid UserUpdateRequest request) {
        User user = getUserByIdOrThrow(userId);
        validatePassword(request, user);
        validatePermission(authUser, userId);

        if (!request.getEmail().equals(user.getEmail())) {
            checkDuplicateEmail(request.getEmail());
        }

        updateUserInfo(request, user);

        userRepository.flush();

        return UserDetailResponse.from(UserDto.from(user));
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(AuthUser authUser, Long id) {
        User user = getUserByIdOrThrow(id);
        validatePermission(authUser, id);

        taskRepository.findAllByAssigneeId(id).forEach(Task::softDelete);
        user.softDelete();
    }

    // Team에 추가 할 수 있는 User 조회
    @Transactional
    public List<UserResponse> getAvailableUsers() {
        return userRepository.findAllByTeamIsNull().stream()
                .map(UserDto::from)
                .map(UserResponse::from)
                .toList();
    }

    // User 찾기 (없으면 예외 발생)
    private User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    // User가 권한이 없으면 예외 발생
    private void validatePermission(AuthUser authUser, Long id) {
        if (!authUser.getId().equals(id)) {
            throw new UserException(ErrorCode.USER_ACCESS_DENIED);
        }
    }

    // 이미 존재하는 Email이면 예외 발생
    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(ErrorCode.USER_DUPLICATE_EMAIL);
        }
    }

    // 이미 존재하는 Username이면 예외 발생
    private void checkDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserException(ErrorCode.USER_DUPLICATE_USERNAME);
        }
    }

    // 비밀번호 불일치시 예외 발생
    private void validatePassword(UserUpdateRequest request, User user) {
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
        }
    }

    // User 정보 수정
    private static void updateUserInfo(UserUpdateRequest request, User user) {
        if (!request.getName().equals(user.getName())) {
            user.updateName(request.getName());
        }
        if (!request.getEmail().equals(user.getEmail())) {
            user.updateEmail(request.getEmail());
        }
    }
}

