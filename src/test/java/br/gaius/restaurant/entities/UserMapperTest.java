package br.gaius.restaurant.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;

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

        UpdateUserDTO dto = new UpdateUserDTO(id, "joaquim5070", "joa.quim@test.com.br", "joaquim",
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
}
