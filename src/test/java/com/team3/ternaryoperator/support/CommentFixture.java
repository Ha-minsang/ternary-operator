package com.team3.ternaryoperator.support;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.User;

public class CommentFixture {

    private static final String DEFAULT_CONTENT = "test comment";

    public static Comment createComment() {
        User user = UserFixture.createUser();
        Task task = TaskFixture.createTask();
        return new Comment(DEFAULT_CONTENT, user, task, null);
    }

    public static Comment createReplyComment(Comment parentComment) {
        User user = UserFixture.createUser();
        Task task = TaskFixture.createTask();
        return new Comment(DEFAULT_CONTENT, user, task, parentComment);
    }
}
