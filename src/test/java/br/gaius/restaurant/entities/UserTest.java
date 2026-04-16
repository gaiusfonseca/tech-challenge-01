package br.gaius.restaurant.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {
    
    @Test
    void should_ReturnCustomer_When_CustomerUser() {
        //given
        String expected = "Customer";
        String actual;
        User user = new CustomerUser("pdr451000", "7cNXUFwC", "pedro@gmail.com", "pedro", "rua das acácias, 12");

        //when
        actual = user.getType();

        //then
        assertEquals(expected, actual);
    }

    @Test
    void should_ReturnOwner_When_OwnerUser() {
        //given
        String expected = "Owner";
        String actual;
        User user = new OwnerUser("pdr451000", "7cNXUFwC", "pedro@gmail.com", "pedro", "rua das acácias, 12");

        //when
        actual = user.getType();

        //then
        assertEquals(expected, actual);
    }
}
