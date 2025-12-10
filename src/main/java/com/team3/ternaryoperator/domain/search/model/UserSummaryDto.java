package com.team3.ternaryoperator.domain.search.model;

import com.team3.ternaryoperator.common.entity.User;
import lombok.Getter;

@Getter
public class UserSummaryDto {

    private final Long id;
    private final String name;
    private final String username;

    public UserSummaryDto(Long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public static UserSummaryDto from(User user) {
        return new UserSummaryDto(user.getId(), user.getName(), user.getUsername());
    }
}
