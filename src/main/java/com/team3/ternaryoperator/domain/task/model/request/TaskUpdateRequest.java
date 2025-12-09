package com.team3.ternaryoperator.domain.task.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TaskUpdateRequest {
    @NotBlank(message = "제목을 작성해주세요.")
    @Size(max = 100, message = "100이하로 작성해주세요.")
    private String title;

    @NotBlank(message = "설명을 작성해주세요.")
    @Size(max = 255, message = "255이하로 작성해주세요.")
    private String description;

    @NotBlank(message = "상태를 입력해주세요.")
    private String status;

    @NotBlank(message = "우선순위를 입력해주세요.")
    private String priority;

    @NotBlank(message = "담당자를 입력해주세요.")
    private Long assigneeId;

    @NotBlank(message = "마감일을 선택해주세요.")
    private LocalDateTime dueDate;
}
