package com.team3.ternaryoperator.domain.comment.dto;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.domain.task.model.dto.TaskDto;
import com.team3.ternaryoperator.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDto {

    private final Long id;
    private final UserDto user;
    private final TaskDto task;
    private final String content;
    private final CommentDto parentComment;

    public static CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                UserDto.from(comment.getUser()),
                TaskDto.from(comment.getTask()),
                comment.getContent(),
                comment.getParentComment() != null
                        ? CommentDto.from(comment.getParentComment())
                        : null
        );
    }
}
// CommentDto 생성