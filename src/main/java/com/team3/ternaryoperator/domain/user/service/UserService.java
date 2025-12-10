package com.team3.ternaryoperator.domain.user.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
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

    @Transactional
    public UserResponse signUp(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getName(),
                encodedPassword,
                UserRole.USER,
                null
        );

        User saved = userRepository.save(user);
        UserDto userDto = UserDto.from(saved);
        return UserResponse.from(userDto);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUser(Long id) {
        User user = getUserByIdOrThrow(id);
        UserDto userDto = UserDto.from(user);
        return UserDetailResponse.from(userDto);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream()
                .map(UserDto::from)
                .toList();

        return userDtoList.stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserDetailResponse updateUser(AuthUser authUser, Long id, @Valid UserUpdateRequest request) {
        User user = getUserByIdOrThrow(id);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (!authUser.getId().equals(id)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (request.getName() != null) {
            user.updateName(request.getName());
        }
        if (request.getEmail() != null) {
            user.updateEmail(request.getEmail());
        }

        userRepository.flush();
        UserDto userDto = UserDto.from(user);
        return UserDetailResponse.from(userDto);
    }

    @Transactional
    public void deleteUser(AuthUser authUser, Long id) {

        User user = getUserByIdOrThrow(id);

        if (!authUser.getId().equals(id)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        List<Task> tasks = taskRepository.findAllByUserId(id);
        for (Task task : tasks) {
            task.softDelete();
        }
        user.softDelete();
    }

    @Transactional
    public List<UserResponse> getAvailableUsers(Long teamId) {
        List<User> userList = userRepository.findByTeamIdNot(teamId);
        List<UserDto> userDtoList = userList.stream()
                .map(UserDto::from)
                .toList();

        return userDtoList.stream()
                .map(UserResponse::from)
                .toList();
    }

    private User getUserByIdOrThrow(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
}
