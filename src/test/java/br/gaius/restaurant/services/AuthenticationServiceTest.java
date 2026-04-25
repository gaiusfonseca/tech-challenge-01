package br.gaius.restaurant.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.exceptions.InvalidPasswordException;
import br.gaius.restaurant.exceptions.UserNotFoundException;
import br.gaius.restaurant.repositories.UserRepository;

@ActiveProfiles("test")
public class AuthenticationServiceTest {

    private UserRepository repository;
    private AuthenticationService service;

    @BeforeEach
    void setup() {
        repository = mock(UserRepository.class);
        service = new AuthenticationService(repository);
    }

    @Test
    void shouldAuthenticateWhenLoginAndPasswordMatches() {
        // given
        String login = "joaquim5070";
        String password = "y2Ne57P";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User candidate = User.builder()
                .withLogin(login)
                .withPassword(password)
                .build();

        User user = User.builder()
                .withLogin(login)
                .withPassword(hashedPassword)
                .build();

        when(repository.findByLogin(login)).thenReturn(Optional.of(user));

        // when
        service.authenticate(candidate);

        // then
        verify(repository).findByLogin(anyString());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenLoginDoesntExist(){
        // given
        String login = "joaquim5070";
        String password = "y2Ne57P";

        User candidate = User.builder()
                .withLogin(login)
                .withPassword(password)
                .build();
        
        when(repository.findByLogin("not exists")).thenReturn(Optional.ofNullable(null));

        // when
        Executable authenticate = () -> service.authenticate(candidate);

        // then
        assertThrows(UserNotFoundException.class, authenticate);
        verify(repository).findByLogin(anyString());
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenPasswordDoesntMatch() {
        // given
        String login = "joaquim5070";
        String password = "y2Ne57P";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String invalidPassword = "yh6jqi5R";
        
        User candidate = User.builder().withLogin(login).withPassword(invalidPassword).build();
        User user = User.builder().withLogin(login).withPassword(hashedPassword).build();
        
        when(repository.findByLogin(login)).thenReturn(Optional.of(user));

        // when
        Executable authenticate = () -> service.authenticate(candidate);

        // then
        assertThrows(InvalidPasswordException.class, authenticate);
        verify(repository).findByLogin(anyString());
    }
}
