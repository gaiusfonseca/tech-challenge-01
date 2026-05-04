package br.gaius.restaurant.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.dtos.UserResponseDTO;
import br.gaius.restaurant.entities.Role;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.entities.UserMapper;
import br.gaius.restaurant.exceptions.DuplicatedEmailException;
import br.gaius.restaurant.exceptions.DuplicatedLoginException;
import br.gaius.restaurant.exceptions.InvalidPaginationParameterException;
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
        Long id = 7L;
        String login = "mugiwara";
        String password = "supersecretpass";
        String email = "mugiwara.luffy@onepiece.com";
        String name = "luffy";
        String address = "rua do rei dos piratas, 7";
        LocalDate lastModified = LocalDate.now();
        Role role = Role.CUSTOMER;

        User user = User.builder()
                .withId(id)
                .withLogin(login)
                .withPassword(password)
                .withEmail(email)
                .withName(name)
                .withAddress(address)
                .withLastModified(lastModified)
                .withRole(role)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDTO expectedDTO = mapper.to(user);

        // when
        UserResponseDTO actualDTO = service.findById(id);

        // then
        assertEquals(expectedDTO, actualDTO);
        verify(repository).findById(id);
    }

    @Test
    void shouldCalculateOffsetWhenParameterizedFindByName() {
        // given
        String name = "maria";
        int page = 5;
        int size = 10;
        int offset = 40;

        when(repository.findByName(name, size, offset))
                .thenReturn(List.of(User.builder().build(), User.builder().build()));

        // when
        List<UserResponseDTO> dtos = service.findByName(name, page, size);

        // then
        assertEquals(2, dtos.size());
        verify(repository).findByName(anyString(), eq(size), eq(offset));
    }

    @Test
    void shouldThrowInvalidParamsExceptionWhenInvalidParams() {
        // given
        String name = "maria";
        int invalidPage = -1;
        int invalidSize = -1;

        // when
        Executable findByNameWithInvalidParams = () -> service.findByName(name, invalidPage, invalidSize);

        // then
        assertThrows(InvalidPaginationParameterException.class, findByNameWithInvalidParams);
    }

    @Test
    void shouldCalculateOffsetWhenParameterizedFindAll() {
        // given
        int size = 10;
        int page = 5;
        int offset = 40;

        when(repository.findAll(size, offset)).thenReturn(List.of());

        // when
        List<UserResponseDTO> dtos = service.findAll(page, size);

        // then
        assertEquals(0, dtos.size());
        verify(repository).findAll(size, offset);
    }

    @Test
    void shouldCalculateOffsetWhenInvalidParamsFindAll() {
        // given
        int invalidPage = -1;
        int invalidSize = -1;

        // when
        Executable findAllWithInvalidParams = () -> service.findAll(invalidPage, invalidSize);

        // then
        assertThrows(InvalidPaginationParameterException.class, findAllWithInvalidParams);
    }

    @Test
    void shouldSaveWhenNoDuplicateEmailAndLogin() {
        // given
        String notDuplicatedEmail = "jacinto@test.com.br";
        CreateUserDTO createDTO = new CreateUserDTO("jacintoXT", "nqEag0T2", notDuplicatedEmail, "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        User user = mapper.from(createDTO);
        User userWithId = User.builder(user).withId(7L).build();
        UserResponseDTO expected = mapper.to(userWithId);

        when(repository.save(user)).thenReturn(Optional.of(userWithId));

        // when
        UserResponseDTO actual = service.save(createDTO);

        // then
        assertEquals(expected, actual);
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

        UpdateUserDTO dto = new UpdateUserDTO("jacintoXT", "jacinto@test.com.br", "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        User user = mapper.from(id, dto);
        UserResponseDTO expected = mapper.to(user);

        when(repository.update(user)).thenReturn(Optional.of(user));

        // when
        UserResponseDTO actual = service.update(id, dto);

        // then
        assertEquals(expected, actual);
        verify(repository).update(user);
    }

    @Test
    void shouldThrowDuplicatedEmailExceptionWhenDuplicatedEmail() {
        // given
        Long id = 7L;
        String fieldName = "email";
        String duplicatedEmail = "pedro@gmail.com";

        UpdateUserDTO dto = new UpdateUserDTO("jacintoXT", duplicatedEmail, "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        when(repository.count(fieldName, duplicatedEmail)).thenReturn(1L);

        // when
        Executable updateWithDuplicatedEmail = () -> service.update(id, dto);

        // then
        assertThrows(DuplicatedEmailException.class, updateWithDuplicatedEmail);
        verify(repository).count(fieldName, duplicatedEmail);
    }

    @Test
    void shouldThrowDuplicatedLoginExceptionWhenDuplicatedLogin() {
        // given
        Long id = 7L;
        String fieldName = "login";
        String duplicatedLogin = "pedro321";

        UpdateUserDTO dto = new UpdateUserDTO(duplicatedLogin, "jacinto@test.com.br", "jacinto",
                "rua dos pés de jaca, 146", Role.OWNER);

        when(repository.count(fieldName, duplicatedLogin)).thenReturn(1L);

        // when
        Executable updateWithDuplicatedLogin = () -> service.update(id, dto);

        // then
        assertThrows(DuplicatedLoginException.class, updateWithDuplicatedLogin);
        verify(repository).count(fieldName, duplicatedLogin);
    }

    @Test
    void shouldSendHashedPassword() {
        // given
        Long id = 4L;
        ChangePasswordDTO dto = new ChangePasswordDTO("c3ZpnpUq", "Wc78utXo");
        User user = User.builder().withId(id).withPassword(dto.oldPassword()).build();
        
        ArgumentCaptor<String> hashed = ArgumentCaptor.forClass(String.class);

        when(repository.findById(id)).thenReturn(Optional.of(user));

        // when
        service.updatePassword(id, dto);

        // then
        verify(repository).updatePassword(eq(user.getId()), hashed.capture());
        assertTrue(BCrypt.checkpw(dto.newPassword(), hashed.getValue()));
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
