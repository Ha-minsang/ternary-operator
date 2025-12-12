package com.team3.ternaryoperator.domain.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentUpdateRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
