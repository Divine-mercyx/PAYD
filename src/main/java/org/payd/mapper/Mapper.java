package org.payd.mapper;

import org.payd.data.models.User;
import org.payd.dtos.responses.CreateUserResponse;

public class Mapper {

    public static CreateUserResponse toCreateUserResponse(User user) {
        return new CreateUserResponse(user.getEmail(), user.getFullName(), user.getPolygonAddress());
    }
}
