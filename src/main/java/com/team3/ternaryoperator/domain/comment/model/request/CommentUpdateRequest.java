package com.team3.ternaryoperator.domain.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotBlank(message = "내용은 비어 있을 수 없습니다.")
    private String content;
}
