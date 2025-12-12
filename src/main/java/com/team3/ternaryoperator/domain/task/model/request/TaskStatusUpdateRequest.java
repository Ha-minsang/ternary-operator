package com.team3.ternaryoperator.domain.task.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdateRequest {
    @NotBlank
    private String status;
}
