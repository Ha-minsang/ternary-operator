package com.team3.ternaryoperator.domain.dashboard.service;

import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendItemResponse;
import com.team3.ternaryoperator.domain.dashboard.model.response.WeeklyTrendResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;

    private static final List<String> WEEKDAYS = List.of("월", "화", "수", "목", "금", "토", "일");

    public List<WeeklyTrendItemResponse> getWeeklyTrend() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<WeeklyTrendItemResponse> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            LocalDate target = startDate.plusDays(i);

            String weekdayName = WEEKDAYS.get(target.getDayOfWeek().getValue() - 1);

            long created = taskRepository.countCreatedByDate(target);
            long completed = taskRepository.countCompletedByDate(target);

            result.add(new WeeklyTrendItemResponse(
                    weekdayName,
                    created,
                    completed,
                    target.toString()
            ));
        }

        return result;
    }
}
