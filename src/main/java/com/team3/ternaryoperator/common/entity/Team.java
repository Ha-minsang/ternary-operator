package com.team3.ternaryoperator.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
@SQLRestriction("deleted_at IS NULL")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateTeamInformation(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
