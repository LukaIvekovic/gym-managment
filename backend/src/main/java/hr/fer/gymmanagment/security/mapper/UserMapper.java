package hr.fer.gymmanagment.security.mapper;

import hr.fer.gymmanagment.security.entity.Role;
import hr.fer.gymmanagment.security.entity.User;
import hr.fer.gymmanagment.security.entity.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserResponse mapToResponse(User user);

    default String map(Role role) {
        return role.getName().name();
    }
}
