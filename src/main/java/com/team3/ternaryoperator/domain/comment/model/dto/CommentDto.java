package com.team3.ternaryoperator.domain.comment.model.dto;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long taskId;
    private Long userId;

    private UserInfo user;
    private String content;
    private Long parentId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private final Long id;
        private final String username;
        private final String name;

        public static UserInfo from(User user) {
            return new UserInfo(
                    user.getId(),
                    user.getUsername(),
                    user.getName()
            );
        }
    }

    public static CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                UserInfo.from(comment.getUser()),
                comment.getContent(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
