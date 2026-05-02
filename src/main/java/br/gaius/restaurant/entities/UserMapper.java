package br.gaius.restaurant.entities;

import org.springframework.stereotype.Component;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.dtos.UserResponseDTO;

@Component
public class UserMapper {

    public User from(CreateUserDTO body) {
        return User.builder()
                .withLogin(body.login())
                .withPassword(body.password())
                .withEmail(body.email())
                .withName(body.name())
                .withAddress(body.address())
                .withRole(body.role())
                .build();
    }

    public User from(Long id, UpdateUserDTO body) {
        return User.builder()
                .withId(id)
                .withLogin(body.login())
                .withEmail(body.email())
                .withName(body.name())
                .withAddress(body.address())
                .withRole(body.role())
                .build();
    }

    public User from(Long id, ChangePasswordDTO body) {
        return User.builder()
                .withId(id)
                .withPassword(body.oldPassword())
                .build();
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getLogin(), user.getEmail(), user.getName(), user.getAddress(),
                user.getLastModified(), user.getRole());
    }

}
