package com.team3.ternaryoperator.domain.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private Long parentId;
}
