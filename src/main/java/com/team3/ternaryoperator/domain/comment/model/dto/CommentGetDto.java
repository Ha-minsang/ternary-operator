package com.team3.ternaryoperator.domain.comment.model.dto;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentGetDto {

    private Long id;
    private String content;
    private Long taskId;
    private Long userId;

    private UserInfo user;
    private Long parentId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private final Long id;
        private final String username;
        private final String name;
        private final String email;
        private final UserRole role;

        public static UserInfo from(User user) {
            return new UserInfo(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole()
            );
        }
    }

    public static CommentGetDto from(Comment comment) {
        return new CommentGetDto(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                UserInfo.from(comment.getUser()),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
