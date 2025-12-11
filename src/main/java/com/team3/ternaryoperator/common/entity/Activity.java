package com.team3.ternaryoperator.common.entity;

// UserDto 임포트는 엔티티 클래스에서 필요 없으므로 제거
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "activities")
@SQLRestriction("deleted_at IS NULL")
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long taskId;
    private String description;
    private LocalDateTime timestamp;


    public Activity(String type,User user,Long taskId, String description) {
        this.type = type;
        this.user = user;
        this.taskId = taskId;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
}