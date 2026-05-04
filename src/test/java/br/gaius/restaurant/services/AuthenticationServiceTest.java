package br.gaius.restaurant.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.dtos.AuthenticationRequestDTO;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.entities.UserFixture;
import br.gaius.restaurant.exceptions.InvalidPasswordException;
import br.gaius.restaurant.exceptions.UserNotFoundException;
import br.gaius.restaurant.repositories.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AuthenticationService service;

    @Test
    void shouldAuthenticateWhenLoginAndPasswordMatches() {
        // given
        AuthenticationRequestDTO dto = UserFixture.getAuthRequestDTO();
        User user = UserFixture.getUser();

        String hashed = BCrypt.hashpw("supersecret", BCrypt.gensalt());
        System.out.println(hashed);
        
        when(repository.findByLogin(dto.login())).thenReturn(Optional.of(user));


        // when
        service.authenticate(dto);

        // then
        verify(repository).findByLogin(dto.login());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenLoginDoesntExist() {
        // given
        AuthenticationRequestDTO dto = UserFixture.getAuthRequestDTO();

        when(repository.findByLogin(dto.login())).thenReturn(Optional.empty());

        // when
        Executable authenticate = () -> service.authenticate(dto);

        // then
        assertThrows(UserNotFoundException.class, authenticate);
        verify(repository).findByLogin(dto.login());
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenPasswordDoesntMatch() {
        // given
        AuthenticationRequestDTO dto = new AuthenticationRequestDTO("mugiwara.luffy", "fakepass");
        User user = UserFixture.getUser();

        when(repository.findByLogin(dto.login())).thenReturn(Optional.of(user));

        // when
        Executable authenticate = () -> service.authenticate(dto);

        // then
        assertThrows(InvalidPasswordException.class, authenticate);
        verify(repository).findByLogin(dto.login());
    }
}
