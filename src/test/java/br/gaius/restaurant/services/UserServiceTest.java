package br.gaius.restaurant.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.entities.Role;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.entities.UserMapper;
import br.gaius.restaurant.exceptions.DuplicatedEmailException;
import br.gaius.restaurant.exceptions.DuplicatedLoginException;
import br.gaius.restaurant.repositories.UserRepository;

@ActiveProfiles("test")
public class UserServiceTest {

    private UserRepository repository;
    private UserMapper mapper;
    private UserService service;

    @BeforeEach
    void setup() {
        repository = mock(UserRepository.class);
        mapper = new UserMapper();
        service = new UserService(repository, mapper);
    }

    @Test
    void shouldFindById() {
        // given
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // when
        service.findById(id);

        // then
        verify(repository).findById(anyLong());
    }

    @Test
    void shouldCalculateOffsetWhenParameterizedFindByName() {
        // given
        String name = "maria";
        int page = 5;
        int size = 10;
        int offset = 40;

        // when
        service.findByName(name, page, size);

        // then
        verify(repository).findByName(anyString(), eq(size), eq(offset));
    }

    @Test
    void shouldCalculateOffsetWhenInvalidParamsFindByName() {
        // given
        String name = "maria";
        int invalidPage = -1;
        int invalidSize = -1;

        int validSize = 10;
        int offset = 0;

        // when
        service.findByName(name, invalidPage, invalidSize);

        // then
        verify(repository).findByName(anyString(), eq(validSize), eq(offset));
    }

    @Test
    void shouldCalculateOffsetWhenNoParamsFindByName() {
        // given
        String name = "maria";
        int size = 10;
        int offset = 0;

        // when
        service.findByName(name);

        // then
        verify(repository).findByName(anyString(), eq(size), eq(offset));
    }

    @Test
    void shouldCalculateOffsetWhenParameterizedFindAll() {
        // given
        int size = 10;
        int page = 5;
        int offset = 40;

        // when
        service.findAll(page, size);

        // then
        verify(repository).findAll(size, offset);
    }

    @Test
    void shouldCalculateOffsetWhenInvalidParamsFindAll() {
        // given
        int invalidPage = -1;
        int invalidSize = -1;

        int validSize = 10;
        int offset = 0;

        // when
        service.findAll(invalidPage, invalidSize);

        // then
        verify(repository).findAll(validSize, offset);
    }

    @Test
    void shouldCalculateOffsetWhenNoParamsFindAll() {
        // given
        int size = 10;
        int offset = 0;

        // when
        service.findAll();

        // then
        verify(repository).findAll(size, offset);
    }

    @Test
    void shouldSaveWhenNoDuplicateEmailAndLogin() {
        // given
        String notDuplicatedEmail = "jacinto@test.com.br";
        CreateUserDTO dto = new CreateUserDTO("jacintoXT", "nqEag0T2", notDuplicatedEmail, "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        User user = mapper.from(dto);
        User userWithId = User.builder(user).withId(7L).build();

        when(repository.save(user)).thenReturn(Optional.of(userWithId));

        // when
        service.save(dto);

        // then
        verify(repository).save(user);
    }

    @Test
    void shouldThrowDuplicatedEmailExceptionWhenSaveDuplicatedEmail() {
        // given
        String fieldName = "email";
        String duplicatedEmail = "pedro@gmail.com";
        CreateUserDTO dto = new CreateUserDTO("jacintoXT", "nqEag0T2", duplicatedEmail, "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);
        when(repository.count(fieldName, duplicatedEmail)).thenReturn(1L);

        // when
        Executable saveShouldThrowException = () -> service.save(dto);

        // then
        assertThrows(DuplicatedEmailException.class, saveShouldThrowException);
        verify(repository).count(fieldName, duplicatedEmail);
    }

    @Test
    void shouldThrowDuplicatedLoginExceptionWhenSaveDuplicatedLogin() {
        // given
        String fieldName = "login";
        String duplicatedLogin = "pedro321";
        CreateUserDTO dto = new CreateUserDTO(duplicatedLogin, "nqEag0T2", "jacinto@test.com.br", "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);
        when(repository.count(fieldName, duplicatedLogin)).thenReturn(1L);

        // when
        Executable saveShouldThrowException = () -> service.save(dto);

        // then
        assertThrows(DuplicatedLoginException.class, saveShouldThrowException);
        verify(repository).count(fieldName, duplicatedLogin);
    }

    @Test
    void shouldUpdateWhenNoDuplicateEmailAndLogin() {
        // given
        Long id = 7L;

        UpdateUserDTO dto = new UpdateUserDTO(id, "jacintoXT", "jacinto@test.com.br", "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        User user = mapper.from(dto);

        when(repository.update(user)).thenReturn(Optional.of(user));

        // when
        service.update(dto);

        // then
        verify(repository).update(user);
    }

    @Test
    void shouldThrowDuplicatedEmailExceptionWhenDuplicatedEmail() {
        // given
        String fieldName = "email";
        String duplicatedEmail = "pedro@gmail.com";

        UpdateUserDTO dto = new UpdateUserDTO(7L, "jacintoXT", duplicatedEmail, "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        when(repository.count(fieldName, duplicatedEmail)).thenReturn(1L);

        // when
        Executable updateWithDuplicatedEmail = () -> service.update(dto);

        // then
        assertThrows(DuplicatedEmailException.class, updateWithDuplicatedEmail);
        verify(repository).count(fieldName, duplicatedEmail);
    }

    @Test
    void shouldThrowDuplicatedLoginExceptionWhenDuplicatedLogin() {
        // given
        String fieldName = "login";
        String duplicatedLogin = "pedro321";

        UpdateUserDTO dto = new UpdateUserDTO(7L, duplicatedLogin, "jacinto@test.com.br", "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        when(repository.count(fieldName, duplicatedLogin)).thenReturn(1L);

        // when
        Executable updateWithDuplicatedLogin = () -> service.update(dto);

        // then
        assertThrows(DuplicatedLoginException.class, updateWithDuplicatedLogin);
        verify(repository).count(fieldName, duplicatedLogin);
    }

    @Test
    void shouldSendHashedPassword() {
        // given
        Long id = 4L;
        ChangePasswordDTO dto = new ChangePasswordDTO("udWP50kX", "r1zjdiSm");
        User user = mapper.from(id, dto);
        
        when(repository.findById(id)).thenReturn(Optional.of(user));

        // when
        service.updatePassword(id, dto);

        // then
        verify(repository).updatePassword(eq(user.getId()), anyString());
    }

    @Test
    void shouldDeleteWhenDelete() {
        // given
        Long id = 1L;

        // when
        service.delete(id);

        // then
        verify(repository).delete(id);
    }
}
