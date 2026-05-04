package br.gaius.restaurant.entities;

import java.time.LocalDate;

import br.gaius.restaurant.dtos.AuthenticationRequestDTO;
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

    public static AuthenticationRequestDTO getAuthRequestDTO() {
        return new AuthenticationRequestDTO("mugiwara.luffy", "supersecret");
    }

    public static User getUser() {
        return User.builder()
            .withId(7L)
            .withLogin("mugiwara")
            .withPassword("$2a$10$l8YeqmuQmkzz1a2RjVE8COZA4oPO6VymmAc7Oh6U/lzHMG0IeMRyG")
            .withEmail("mugiwara.luffy@test.com")
            .withName("luffy")
            .withAddress("rua do rei dos piratas, 7")
            .withLastModified(LocalDate.now())
            .withRole(Role.CUSTOMER)
            .build();
    }
}
