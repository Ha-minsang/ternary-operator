package com.team3.ternaryoperator.domain.dashboard.service;

import com.team3.ternaryoperator.common.dto.AuthUser;
import com.team3.ternaryoperator.domain.dashboard.model.response.DashboardStatsResponse;
import com.team3.ternaryoperator.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats(AuthUser authUser) {
        return taskRepository.findDashboardCounts(authUser.getId());
    }
}
