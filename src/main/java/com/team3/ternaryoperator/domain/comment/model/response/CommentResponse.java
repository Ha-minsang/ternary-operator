package com.team3.ternaryoperator.domain.comment.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team3.ternaryoperator.domain.comment.model.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private final Long id;
    private final Long taskId;
    private final Long userId;
    private final UserInfo user;
    private final String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long parentId;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private final Long id;
        private final String username;
        private final String name;

        public static UserInfo from(CommentDto.UserInfo dto) {
            return new UserInfo(
                    dto.getId(),
                    dto.getUsername(),
                    dto.getName()
            );
        }
    }

    public static CommentResponse from(CommentDto dto) {
        return new CommentResponse(
                dto.getId(),
                dto.getTaskId(),
                dto.getUserId(),
                UserInfo.from(dto.getUser()),
                dto.getContent(),
                dto.getParentId(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}