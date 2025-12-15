package com.team3.ternaryoperator.common.entity;

import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "activities")
@SQLRestriction("deleted_at IS NULL")
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private ActivityType activityType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private Long taskId;

    @Column
    private String description;

    public Activity(ActivityType activityType, User user, Long taskId, String description) {
        this.activityType = activityType;
        this.user = user;
        this.taskId = taskId;
        this.description = description;
    }
}
