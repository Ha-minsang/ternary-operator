package com.team3.ternaryoperator.domain.task.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.dto.PageResponse;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.domain.task.enums.TaskPriority;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.model.dto.TaskDto;
import com.team3.ternaryoperator.domain.task.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.task.model.request.TaskUpdateRequest;
import com.team3.ternaryoperator.domain.task.model.response.AssigneeResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskDetailResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskGetResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        User assignee = getUserByIdOrThrow(id);
        TaskPriority taskPriority = TaskPriority.valueOf(request.getPriority());
        Task task = taskRepository.save(new Task(request.getTitle(), request.getDescription(), TaskStatus.TODO, taskPriority, assignee, request.getDueDate()));
        TaskDto dto = TaskDto.from(task);
        return TaskResponse.from(dto);
    }

    // 작업 수정
    @Transactional
    public TaskResponse updateTask(Long userId, Long taskId, TaskUpdateRequest request) {
        User assignee = getUserByIdOrThrow(userId);
        Task task = getTaskByIdOrThrow(taskId);
        matchedAssignee(assignee.getId(), task.getAssignee().getId());
        task.update(request);
        taskRepository.save(task);
        TaskDto dto = TaskDto.from(task);
        return TaskResponse.from(dto);
    }

    // 작업 상세 조회
    @Transactional(readOnly = true)
    public TaskDetailResponse getOneTask(Long id) {
        Task task = getTaskByIdOrThrow(id);
        User assignee = getUserByIdOrThrow(task.getAssignee().getId());
        return TaskDetailResponse.from(TaskDto.from(task), AssigneeResponse.from(assignee));
    }

    // 작업 목록 조회
    @Transactional(readOnly = true)
    public PageResponse<TaskGetResponse> getAllTask(String status, String search, Long assigneeId, Pageable pageable) {
        Page<Task> taskPage = taskRepository.getTasks(status, search, assigneeId, pageable);
        Page<TaskDto> taskList = taskPage.map(TaskDto::from);
        Page<TaskGetResponse> taskPageList = taskList.map(taskDto -> TaskGetResponse.from(taskDto, AssigneeResponse.from(taskDto.getAssignee())));
        return PageResponse.from(taskPageList);
    }

    // 작업 삭제
    @Transactional
    public void deleteTask(AuthUser authUser, Long id) {
        Task task = getTaskByIdOrThrow(id);
        User assignee = getUserByIdOrThrow(authUser.getId());
        matchedAssignee(assignee.getId(), task.getAssignee().getId());
        task.softDelete();
    }

    // 유저 아이디가 일치하는 유저가 없으면 예외처리
    private User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
    }

    // 작업 아이디가 일치하는 작업이 없으면 예외처리
    private Task getTaskByIdOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new CustomException(TASK_NOT_FOUND)
        );
    }

    // 담당자 일치 확인
    private void matchedAssignee(Long assigneeId, Long taskUserId) {
        if(!assigneeId.equals(taskUserId)) {
            throw new CustomException(TASK_FORBIDDEN_ONLY_ASSIGNEE);
        }
    }
}