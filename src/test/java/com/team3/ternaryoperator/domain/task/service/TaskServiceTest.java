package com.team3.ternaryoperator.domain.task.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.common.exception.CustomException;
import com.team3.ternaryoperator.common.exception.ErrorCode;
import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import com.team3.ternaryoperator.domain.activity.service.ActivityService;
import com.team3.ternaryoperator.domain.task.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.task.model.request.TaskStatusUpdateRequest;
import com.team3.ternaryoperator.domain.task.model.request.TaskUpdateRequest;
import com.team3.ternaryoperator.domain.task.model.response.TaskDetailResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskGetResponse;
import com.team3.ternaryoperator.domain.task.model.response.TaskResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivityService activityService;

    @InjectMocks
    private TaskService service;

    @Test
    @DisplayName("작업 생성 성공")
    public void createTask_success() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        TaskCreateRequest request = new TaskCreateRequest(
                "test title",
                "test description",
                "MEDIUM",
                authUser.getId(),
                LocalDateTime.of(2025, 12, 25, 7, 30)
        );

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", 1L);
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(assignee));
        Task savedTask = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(savedTask, "id", 1L);
        given(taskRepository.save(any(Task.class))).willReturn(savedTask);
        doNothing().when(activityService).saveActivity(any(ActivityType.class), any(Long.class), any(Long.class), any(String.class));

        // when
        TaskResponse result = service.createTask(authUser, request);

        // then
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getPriority(), result.getPriority());
        assertEquals(request.getAssigneeId(), result.getAssignee().getId());
        assertEquals(request.getDueDate(), result.getDueDate());
    }

    @Test
    @DisplayName("작업 생성 실패 - 담당자의 id가 없는 경우 USER_NOT_FOUND")
    public void createTask_fail_userNotFound() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        TaskCreateRequest request = new TaskCreateRequest(
                "제목",
                "설명",
                "HIGH",
                authUser.getId(),
                LocalDateTime.of(2025, 12, 25, 7, 30)
        );

        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> service.createTask(authUser, request)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 수정 성공")
    public void updateTask_success() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        Long newAssigneeId = 2L;
        TaskUpdateRequest request = new TaskUpdateRequest(
                "제목",
                "설명",
                "TODO",
                "HIGH",
                newAssigneeId,
                LocalDateTime.of(2025, 12, 25, 7, 30)
        );

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(assignee));

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));

        User newAssignee = UserFixture.createUser();
        ReflectionTestUtils.setField(newAssignee, "id", newAssigneeId);
        given(userRepository.findById(request.getAssigneeId())).willReturn(Optional.of(newAssignee));
        given(taskRepository.save(any(Task.class))).willAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(activityService).saveActivity(any(ActivityType.class), any(Long.class), any(Long.class), any(String.class));

        // when
        TaskResponse result = service.updateTask(authUser, taskId, request);

        // then
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getStatus(), result.getStatus());
        assertEquals(request.getPriority(), result.getPriority());
        assertEquals(request.getAssigneeId(), result.getAssignee().getId());
        assertEquals(request.getDueDate(), result.getDueDate());
    }

    @Test
    @DisplayName("작업 수정 실패 - 현재 담당자의 id가 없는 경우 USER_NOT_FOUND")
    public void updateTask_fail_current_assigneeId_userNotFound() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        Long newAssigneeId = 2L;
        TaskUpdateRequest request = new TaskUpdateRequest(
                "제목",
                "설명",
                "TODO",
                "HIGH",
                newAssigneeId,
                LocalDateTime.of(2025, 12, 25, 7, 30)
        );

        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> service.updateTask(authUser, taskId, request)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 수정 실패 - 작업 id가 없는 경우 TASK_NOT_FOUND")
    public void updateTask_fail_taskId_taskNotFound() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        Long newAssigneeId = 2L;
        TaskUpdateRequest request = new TaskUpdateRequest(
                "제목",
                "설명",
                "TODO",
                "HIGH",
                newAssigneeId,
                LocalDateTime.of(2025, 12, 25, 7, 30)
        );

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(assignee));

        given(taskRepository.findById(taskId)).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> service.updateTask(authUser, taskId, request)
        );

        // then
        assertEquals(ErrorCode.TASK_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 수정 실패 - 새로운 담당자의 id가 없는 경우 USER_NOT_FOUND")
    public void updateTask_fail_new_assigneeId_userNotFound() {
        // given
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        Long newAssigneeId = 2L;
        TaskUpdateRequest request = new TaskUpdateRequest(
                "제목",
                "설명",
                "TODO",
                "HIGH",
                newAssigneeId,
                LocalDateTime.of(2025, 12, 25, 7, 30)
        );

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(assignee));

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));

        given(userRepository.findById(request.getAssigneeId())).willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> service.updateTask(authUser, taskId, request)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 상세 조회 성공")
    public void getOneTask_success() {
        // give
        Long taskId = 1L;
        Long assigneeId = 1L;

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", assigneeId);

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(userRepository.findById(assigneeId)).willReturn(Optional.of(assignee));
        // when
        TaskDetailResponse result = service.getOneTask(taskId);

        // then
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getStatus().name(), result.getStatus());
        assertEquals(task.getPriority().name(), result.getPriority());
        assertEquals(task.getAssignee(), assignee);
        assertEquals(assigneeId, result.getAssignee().getId());
        assertEquals(task.getDueDate(), result.getDueDate());
    }

    @Test
    @DisplayName("작업 상세 조회 실패 - 해당 작업 id가 없을 경우")
    public void getOneTask_fail_taskId_taskNotFound() {
        // give
        Long taskId = 1L;
        Long assigneeId = 1L;

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", assigneeId);

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);

        // when
        given(taskRepository.findById(taskId)).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> service.getOneTask(taskId)
        );

        // then
        assertEquals(ErrorCode.TASK_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 상세 조회 실패 - 해당 작업의 유저 id가 없을 경우")
    public void getOneTask_fail_userId_userNotFound() {
        // give
        Long taskId = 1L;
        Long assigneeId = 1L;

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", assigneeId);

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);

        // when
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        given(userRepository.findById(assigneeId)).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> service.getOneTask(taskId)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 상태 변경 성공")
    public void updateTaskStatus_success() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(
                "IN_PROGRESS"
        );

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(assignee));

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        doNothing().when(activityService).saveActivity(any(ActivityType.class), any(Long.class), any(Long.class), any(String.class));

        // when
        given(taskRepository.save(any(Task.class))).willAnswer(invocation -> invocation.getArgument(0));
        TaskGetResponse result = service.updateTaskStatus(authUser, taskId, request);

        // then
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getStatus().name(), result.getStatus());
        assertEquals(task.getPriority().name(), result.getPriority());
        assertEquals(task.getAssignee(), assignee);
        assertEquals(task.getAssignee().getId(), result.getAssignee().getId());
        assertEquals(task.getDueDate(), result.getDueDate());
    }

    @Test
    @DisplayName("작업 상태 변경 실패 - 순서대로 변경 하지 않을 경우")
    public void updateTaskStatus_fail_sequence() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(
                "DONE"
        );

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(assignee));

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));

        // then
        CustomException exception = assertThrows(CustomException.class,
                () -> service.updateTaskStatus(authUser, taskId, request)
        );

        // then
        assertEquals(ErrorCode.TASK_INVALID_STATUS_FLOW, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 상태 변경 실패 - 해당 작업 id가 없을 경우")
    public void updateTaskStatus_fail_taskId_taskNotFound() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(
                "IN_PROGRESS"
        );

        given(taskRepository.findById(taskId)).willReturn(Optional.empty());

        // then
        CustomException exception = assertThrows(CustomException.class,
                () -> service.updateTaskStatus(authUser, taskId, request)
        );

        // then
        assertEquals(ErrorCode.TASK_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 상태 변경 실패 - 유저 id가 없을 경우")
    public void updateTaskStatus_fail_userId_userNotFound() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(
                "IN_PROGRESS"
        );

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));

        // then
        CustomException exception = assertThrows(CustomException.class,
                () -> service.updateTaskStatus(authUser, taskId, request)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 삭제 성공")
    public void deleteTask_success() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(assignee));

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        doNothing().when(activityService).saveActivity(any(ActivityType.class), any(Long.class), any(Long.class), any(String.class));
        
        // when
        assertDoesNotThrow(() -> service.deleteTask(authUser, taskId));

        // then
        assertNotNull(task.getDeletedAt());
    }

    @Test
    @DisplayName("작업 삭제 실패 - 해당 작업 id가 없을 경우")
    public void deleteTask_fail_taskId_taskNotFound() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;

        given(taskRepository.findById(taskId)).willReturn(Optional.empty());

        // then
        CustomException exception = assertThrows(CustomException.class,
                () -> service.deleteTask(authUser, taskId)
        );

        // then
        assertEquals(ErrorCode.TASK_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("작업 삭제 실패 - 유저 id가 없을 경우")
    public void deleteTask_fail_userId_userNotFound() {
        // given
        AuthUser authUser = AuthUserFixture.createAuthUser();
        Long taskId = 1L;

        User assignee = UserFixture.createUser();
        ReflectionTestUtils.setField(assignee, "id", authUser.getId());
        given(userRepository.findById(authUser.getId())).willReturn(Optional.empty());

        Task task = TaskFixture.createTask(assignee);
        ReflectionTestUtils.setField(task, "id", taskId);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
        // then
        CustomException exception = assertThrows(CustomException.class,
                () -> service.deleteTask(authUser, taskId)
        );

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}