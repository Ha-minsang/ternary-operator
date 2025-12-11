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

    @Column
    private Long userId;

    @Column
    private Long taskId;
}
