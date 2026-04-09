package br.gaius.restaurant.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.entities.User;

@JdbcTest
@ActiveProfiles("test")
public class UserRepositoryDataHandlerTest {
    
    @Autowired
    private JdbcClient jdbcClient;

    private UserRepositoryDataHandler repository;

    @BeforeEach
    void setup(){
        repository = new UserRepositoryDataHandler(jdbcClient);
    }

    @Test
    void should_FindUser_When_UserExists() {
        // given
        Optional<User> expected = Optional.of(new User("pedrinho@gmail.com", "pedro", "pcaldeira", "pcdr1234", "rua das acácias"));
        Optional<User> actual;
        
        // when
        actual = repository.findById(expected.get().getEmail());

        // then
        assertEquals(expected, actual);
    }
}
