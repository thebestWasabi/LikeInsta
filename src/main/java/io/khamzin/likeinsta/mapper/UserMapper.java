package io.khamzin.likeinsta.mapper;

import io.khamzin.likeinsta.dto.UserDTO;
import io.khamzin.likeinsta.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getName())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .bio(user.getBio())
                .build();
    }

}
