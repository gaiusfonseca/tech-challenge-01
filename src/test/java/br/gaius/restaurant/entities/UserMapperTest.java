package br.gaius.restaurant.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.dtos.UserResponseDTO;

public class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new UserMapper();
    }

    @Test
    void shouldCreateUserFromCreateDTO() {
        // given
        CreateUserDTO dto = new CreateUserDTO("joaquim5070", "y2Ne57P", "joa.quim@test.com.br", "joaquim",
                "rua das pitobeiras, 3914", Role.OWNER);
        User expected = User.builder()
                .withLogin("joaquim5070")
                .withPassword("y2Ne57P")
                .withEmail("joa.quim@test.com.br")
                .withName("joaquim")
                .withAddress("rua das pitobeiras, 3914")
                .withRole(Role.OWNER)
                .build();

        // when
        User actual = mapper.from(dto);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateUserFromUpdateDTO() {
        // given
        Long id = 7L;
        User expected = User.builder()
                .withId(id)
                .withLogin("joaquim5070")
                .withEmail("joa.quim@test.com.br")
                .withName("joaquim")
                .withAddress("rua das pitombeiras, 3914")
                .withRole(Role.OWNER)
                .build();

        UpdateUserDTO dto = new UpdateUserDTO("joaquim5070", "joa.quim@test.com.br", "joaquim",
                "rua das pitombeiras, 3914", Role.OWNER);

        // when
        User actual = mapper.from(id, dto);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateUserFromChangePasswordDTO() {
        // given
        Long id = 7L;
        User expceted = User.builder()
                .withId(id)
                .withPassword("y2Ne57P")
                .build();

        ChangePasswordDTO dto = new ChangePasswordDTO("y2Ne57P", "2LzzYl8q");

        // when
        User actual = mapper.from(id, dto);

        // then
        assertEquals(expceted, actual);
    }

    @Test
    void shouldCreateResponseDTOFromUser() {
        // given
        Long id = 7L;
        String login = "mugiwara.luffy";
        String password = "supersecret123";
        String email = "luffy.strawhat@test.com";
        String name = "luffy";
        String address = "rua do rei dos piratas, 7";
        LocalDate lastModified = LocalDate.now();
        Role role = Role.CUSTOMER;

        User user = User
                .builder()
                .withId(id)
                .withLogin(login)
                .withPassword(password)
                .withEmail(email)
                .withName(name)
                .withAddress(address)
                .withLastModified(lastModified)
                .withRole(role)
                .build();

        UserResponseDTO expected = new UserResponseDTO(id, login, email, name, address, lastModified, role);

        // when
        UserResponseDTO actual = mapper.toResponseDTO(user);

        // then
        assertEquals(expected, actual);
    }
}
