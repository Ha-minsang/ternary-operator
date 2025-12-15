package com.team3.ternaryoperator.domain.comment.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team3.ternaryoperator.domain.comment.model.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentUpdateResponse {

    private final Long id;
    private final Long taskId;
    private final Long userId;
    private final String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long parentId;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentUpdateResponse from(CommentDto dto) {
        return new CommentUpdateResponse(
                dto.getId(),
                dto.getTaskId(),
                dto.getUserId(),
                dto.getContent(),
                dto.getParentId(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

}
