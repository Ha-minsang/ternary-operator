package com.team3.ternaryoperator.domain.activity.model.condition;

import com.team3.ternaryoperator.domain.activity.enums.ActivityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ActivitySearchCond {

    private ActivityType activityType;
    private Long taskId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
