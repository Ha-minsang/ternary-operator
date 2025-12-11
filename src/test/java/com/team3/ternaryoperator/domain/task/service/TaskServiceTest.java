package com.team3.ternaryoperator.domain.task.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.task.enums.TaskPriority;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.model.request.TaskCreateRequest;
import com.team3.ternaryoperator.domain.task.model.response.TaskResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.user.enums.UserRole;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskService service;


    @Test
    @DisplayName("작업 생성 성공")
    public void createTask_success() {
        // given
        AuthUser authUser = new AuthUser(1L, "test", UserRole.USER);
        TaskCreateRequest request = new TaskCreateRequest("제목", "설명", "HIGH", authUser.getId(), LocalDateTime.of(2025, 10, 22, 10, 2));

        User assignee = mock(User.class);
        // assignee.getId()를 보내면 항상 authUser.getId() 내놔
        when(assignee.getId()).thenReturn(authUser.getId());
        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(assignee));

        // when
        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                TaskStatus.TODO,
                TaskPriority.valueOf(request.getPriority()),
                assignee,
                request.getDueDate()
        );

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskResponse result = service.createTask(authUser, request);

        // then
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getPriority(), result.getPriority());
        assertEquals(request.getAssigneeId(), result.getAssignee().getId());
        assertEquals(request.getDueDate(), result.getDueDate());
    }
}