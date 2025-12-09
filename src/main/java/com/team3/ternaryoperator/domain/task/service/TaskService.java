package com.team3.ternaryoperator.domain.task.service;


import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.domain.task.enums.TaskPriority;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.model.dto.TaskDto;
import com.team3.ternaryoperator.domain.task.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.task.model.request.TaskUpdateRequest;
import com.team3.ternaryoperator.domain.task.model.response.TaskResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.team3.ternaryoperator.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // 작업 생성
    @Transactional
    public TaskResponse createTask(Long id, TaskCreateRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        TaskPriority taskPriority = TaskPriority.valueOf(request.getPriority());
        Task task = taskRepository.save(new Task(request.getTitle(), request.getDescription(), TaskStatus.TODO, taskPriority, user, request.getDueDate()));
        TaskDto dto = TaskDto.from(task);
        return TaskResponse.from(dto);
    }

    // 작업 수정
    @Transactional
    public TaskResponse updateTask(Long userId, Long taskId, TaskUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        Task task = taskRepository.findTaskById(taskId);
        matchedAssignee(user.getId(), task.getAssignee().getId());
        task.update(request);
        taskRepository.save(task);
        TaskDto dto = TaskDto.from(task);
        return TaskResponse.from(dto);
    }


    // 작성자 일치 확인
    private void matchedAssignee(Long assigneeId, Long taskUserId) {
        if(!assigneeId.equals(taskUserId)) {
            throw new CustomException(TASK_FORBIDDEN_ONLY_ASSIGNEE);
        }
    }
}