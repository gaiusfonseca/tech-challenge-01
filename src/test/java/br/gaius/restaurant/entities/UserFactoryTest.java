package br.gaius.restaurant.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.gaius.restaurant.dtos.CreateUserDTO;

public class UserFactoryTest {
    
    @Test
    void should_ReturnACustomerUser_When_CustomerUserFactory() {
        //given
        UserFactory userFactory = new CustomerUserFactory();
        CreateUserDTO createUserDTO = new CreateUserDTO(
            "pdr451000", "7cNXUFwC", "pedro@gmail.com", "pedro", "rua das acácias, 12"
        );
        String expected = "Customer";
        String actual;

        //when
        User customerUser = userFactory.of(createUserDTO);
        actual = customerUser.getType();

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_ReturnAnOwnerUser_When_OwnerUserFactory() {
        //given
        UserFactory userFactory = new OwnerUserFactory();
        CreateUserDTO createUserDTO = new CreateUserDTO(
            "pdr451000", "7cNXUFwC", "pedro@gmail.com", "pedro", "rua das acácias, 12"
        );
        String expected = "Owner";
        String actual;

        //when
        User customerUser = userFactory.of(createUserDTO);
        actual = customerUser.getType();

        //then
        assertEquals(expected, actual);
    }
}
