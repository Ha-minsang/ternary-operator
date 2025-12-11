package com.team3.ternaryoperator.common.config;

import com.team3.ternaryoperator.common.entity.Comment;
import com.team3.ternaryoperator.common.entity.Task;
import com.team3.ternaryoperator.common.entity.Team;
import com.team3.ternaryoperator.common.entity.User;
import com.team3.ternaryoperator.domain.comment.repository.CommentRepository;
import com.team3.ternaryoperator.domain.task.enums.TaskPriority;
import com.team3.ternaryoperator.domain.task.enums.TaskStatus;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import com.team3.ternaryoperator.domain.team.repository.TeamRepository;
import com.team3.ternaryoperator.domain.user.enums.UserRole;
import com.team3.ternaryoperator.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1. 팀 3개 생성
        Team teamA = new Team("Team Alpha", "알파 팀입니다.");
        Team teamB = new Team("Team Beta", "베타 팀입니다.");
        Team teamC = new Team("Team Gamma", "감마 팀입니다.");

        teamRepository.saveAll(List.of(teamA, teamB, teamC));

        // 2. 유저 7명 생성 (권한은 모두 USER)
        List<User> users = new ArrayList<>();

        // teamA: 2명
        users.add(new User(
                "user1",
                "user1@example.com",
                "User One",
                passwordEncoder.encode("password1!"),
                UserRole.USER,
                teamA
        ));
        users.add(new User(
                "user2",
                "user2@example.com",
                "User Two",
                passwordEncoder.encode("password2!"),
                UserRole.USER,
                teamA
        ));

        // teamB: 2명
        users.add(new User(
                "user3",
                "user3@example.com",
                "User Three",
                passwordEncoder.encode("password3!"),
                UserRole.USER,
                teamB
        ));
        users.add(new User(
                "user4",
                "user4@example.com",
                "User Four",
                passwordEncoder.encode("password4!"),
                UserRole.USER,
                teamB
        ));

        // teamC: 2명
        users.add(new User(
                "user5",
                "user5@example.com",
                "User Five",
                passwordEncoder.encode("password5!"),
                UserRole.USER,
                teamC
        ));
        users.add(new User(
                "user6",
                "user6@example.com",
                "User Six",
                passwordEncoder.encode("password6!"),
                UserRole.USER,
                teamC
        ));

        // 팀에 속하지 않는 유저 1명
        users.add(new User(
                "user7",
                "user7@example.com",
                "User Seven",
                passwordEncoder.encode("password7!"),
                UserRole.USER,
                null
        ));

        users = userRepository.saveAll(users);

        // 3. 각 유저당 Task 5개씩 생성 (총 35개)
        //    우선순위(LOW, MEDIUM, HIGH)가 각각 10개 이상 되도록 반복 패턴으로 분배
        List<Task> tasks = new ArrayList<>();

        TaskStatus[] statusPattern = {
                TaskStatus.TODO,
                TaskStatus.IN_PROGRESS,
                TaskStatus.DONE
        };

        TaskPriority[] priorityPattern = {
                TaskPriority.LOW,
                TaskPriority.MEDIUM,
                TaskPriority.HIGH
        };

        int taskIndex = 0;

        for (User user : users) {
            for (int i = 0; i < 5; i++) {
                TaskStatus status = statusPattern[taskIndex % statusPattern.length];
                TaskPriority priority = priorityPattern[taskIndex % priorityPattern.length];
                LocalDateTime dueDate = LocalDateTime.now().plusDays((taskIndex % 10) + 1);

                Task task = new Task(
                        user.getName() + "의 작업 " + (i + 1),
                        "더미 작업 설명 #" + (taskIndex + 1),
                        status,
                        priority,
                        user,
                        dueDate
                );

                tasks.add(task);
                taskIndex++;
            }
        }

        tasks = taskRepository.saveAll(tasks);

        // 4. 각 Task마다 Comment 12개씩 생성 (총 35 * 12 = 420개)
        List<Comment> comments = new ArrayList<>();
        int commentSeed = 0;

        for (Task task : tasks) {
            for (int i = 0; i < 12; i++) {
                // 유저는 적당히 섞어서 배정
                User commentAuthor = users.get((commentSeed + i) % users.size());

                Comment comment = new Comment(
                        "작업 \"" + task.getTitle() + "\" 에 대한 댓글 " + (i + 1),
                        commentAuthor,
                        task,
                        null // parentComment는 전부 null (대댓글 구조는 안 씀)
                );

                comments.add(comment);
            }
            commentSeed++;
        }

        commentRepository.saveAll(comments);
    }
}
