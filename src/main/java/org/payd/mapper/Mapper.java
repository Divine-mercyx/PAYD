package org.payd.mapper;

import org.payd.data.models.User;
import org.payd.dtos.responses.CreateUserResponse;

import java.util.Map;

public class Mapper {

    public static CreateUserResponse toCreateUserResponse(User user, Map<String, String> map) {
        return new CreateUserResponse(
                user.getEmail(),
                user.getFullName(),
                user.getPolygonAddress(),
                map.get("accessToken"),
                map.get("refreshToken")
        );
    }
}
