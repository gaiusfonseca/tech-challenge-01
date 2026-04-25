package br.gaius.restaurant.entities;

import org.springframework.stereotype.Component;

import br.gaius.restaurant.dtos.ChangePassword;
import br.gaius.restaurant.dtos.CreateUser;
import br.gaius.restaurant.dtos.UpdateUser;

@Component
public class UserMapper {

    public User from(CreateUser body) {
        return User.builder()
                .withLogin(body.login())
                .withPassword(body.password())
                .withEmail(body.email())
                .withName(body.name())
                .withAddress(body.address())
                .withRole(body.role())
                .build();
    }

    public User from(UpdateUser body) {
        return User.builder()
                .withId(body.id())
                .withLogin(body.login())
                .withEmail(body.email())
                .withName(body.name())
                .withAddress(body.address())
                .withRole(body.role())
                .build();
    }

    public User from(ChangePassword body) {
        return User.builder()
                .withLogin(body.login())
                .withPassword(body.oldPassword())
                .build();
    }

}
