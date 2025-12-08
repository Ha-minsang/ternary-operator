package com.team3.ternaryoperator.domain.tasks.service;


import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.domain.tasks.enums.TaskPriority;
import com.team3.ternaryoperator.domain.tasks.enums.TaskStatus;
import com.team3.ternaryoperator.domain.tasks.model.dto.TaskDto;
import com.team3.ternaryoperator.domain.tasks.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.tasks.model.response.TaskCreateResponse;
import com.team3.ternaryoperator.domain.tasks.repository.TaskRepository;
import com.team3.ternaryoperator.domain.users.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.team3.ternaryoperator.common.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 작업 생성
    @Transactional
    public TaskCreateResponse createTask(Long id, @Valid TaskCreateRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        TaskPriority taskPriority = TaskPriority.valueOf(request.getPriority());

        Task task = taskRepository.save(new Task(request.getTitle(), request.getDescription(), TaskStatus.TODO, taskPriority, user, request.getDueDate()));
        TaskDto dto = TaskDto.from(task);
        return TaskCreateResponse.from(dto);
    }


}