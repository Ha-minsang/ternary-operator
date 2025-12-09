package com.team3.ternaryoperator.domain.comment.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team3.ternaryoperator.domain.comment.model.dto.CommentDto;
import com.team3.ternaryoperator.domain.comment.model.dto.CommentGetDto;
import com.team3.ternaryoperator.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentGetResponse {

    private final Long id;
    private final String content;
    private final Long taskId;
    private final Long userId;
    private final UserInfo user;

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
        private final String email;
        private final UserRole role;

        public static UserInfo from(CommentGetDto.UserInfo dto) {
            return new UserInfo(
                    dto.getId(),
                    dto.getUsername(),
                    dto.getName(),
                    dto.getEmail(),
                    dto.getRole()
            );
        }
    }

    public static CommentGetResponse from(CommentGetDto dto) {
        return new CommentGetResponse(
                dto.getId(),
                dto.getContent(),
                dto.getTaskId(),
                dto.getUserId(),
                UserInfo.from(dto.getUser()),
                dto.getParentId(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}