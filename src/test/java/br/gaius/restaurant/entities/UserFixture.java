package br.gaius.restaurant.entities;

import java.time.LocalDate;

import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.dtos.UserResponseDTO;

public class UserFixture {

    public static CreateUserDTO getCreateDTO() {
        return new CreateUserDTO("mugiwara", "supersecret", "mugiwara.luffy@test.com", "luffy",
                "rua do rei dos piratas, 7", Role.CUSTOMER);
    }

    public static UpdateUserDTO getUpdateDTO() {
        return new UpdateUserDTO("mugiwara", "mugiwara.luffy@test.com", "luffy", "rua do rei dos piratas, 7",
                Role.CUSTOMER);

    }

    public static UserResponseDTO getResponseDTO() {
        return new UserResponseDTO(7l, "mugiwara", "mugiwara.luffy@test.com", "luffy",
                "rua do rei dos piratas, 7", LocalDate.now(), Role.CUSTOMER);
    }

    public static User getUser() {
        return User.builder()
            .withId(7L)
            .withLogin("mugiwara")
            .withPassword("supersecret")
            .withEmail("mugiwara.luffy@test.com")
            .withName("luffy")
            .withAddress("rua do rei dos piratas, 7")
            .withLastModified(LocalDate.now())
            .withRole(Role.CUSTOMER)
            .build();
    }
}
