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

    private Long taskId;
    private String description;
    private LocalDateTime timestamp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User actor;


    public Activity(String type, User actor, Long taskId, String description) {
        this.type = type;
        this.actor = actor;
        this.taskId = taskId;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
}