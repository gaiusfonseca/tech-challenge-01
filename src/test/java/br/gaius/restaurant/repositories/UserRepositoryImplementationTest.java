package br.gaius.restaurant.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.entities.Role;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.exceptions.UnsupportedFieldException;

@JdbcTest
@ActiveProfiles("test")
public class UserRepositoryImplementationTest {

    @Autowired
    private JdbcClient jdbcClient;
    private UserRepository repository;

    @BeforeEach
    void setup() {
        repository = new UserRepositoryImplementation(jdbcClient);
    }

    @Test
    void shouldFindWhenExistingId() {
        // given
        Long id = 1L;
        User user = User.builder()
                .withId(id)
                .withLogin("pedro321")
                .withPassword("X321kHEz")
                .withEmail("pedro@gmail.com")
                .withName("pedro")
                .withAddress("rua das acácias, 12")
                .withLastModified(LocalDate.of(2026, 04, 01))
                .withRole(Role.CUSTOMER)
                .build();

        Optional<User> expectedUser = Optional.of(user);

        // when
        Optional<User> actualUser = repository.findById(id);

        // then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldReturnEmptyOptionalWhenNonExistingId() {
        // given
        Long idDoesNotExist = 19L;

        // when
        Optional<User> actual = repository.findById(idDoesNotExist);

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldFindWhenExistingLogin() {
        // given
        String login = "pedro321";

        User user = User.builder()
                .withId(1L)
                .withLogin(login)
                .withPassword("X321kHEz")
                .withEmail("pedro@gmail.com")
                .withName("pedro")
                .withAddress("rua das acácias, 12")
                .withLastModified(LocalDate.of(2026, 04, 01))
                .withRole(Role.CUSTOMER)
                .build();

        Optional<User> expectedUser = Optional.of(user);

        // when
        Optional<User> actualUser = repository.findByLogin(login);

        // then
        assertEquals(expectedUser.get().getLogin(), actualUser.get().getLogin());
    }

    @Test
    void shouldReturnEmptyOptionalWhenNonExistingLogin() {
        // given
        String nonExistingLogin = "Bruce_Wayne";

        // when
        Optional<User> actual = repository.findByLogin(nonExistingLogin);

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldFindOneWhenUniqueName() {
        // given
        String uniqueName = "pedro";
        int size = 10;
        int offset = 0;

        // when
        List<User> users = repository.findByName(uniqueName, size, offset);

        // then
        assertAll(
            () -> assertEquals(1, users.size()),
            () -> assertEquals("pedro", users.get(0).getName())
        );
    }

    @Test
    void shouldFindNoneWhenNonExistingName() {
        // given
        String nonExistingName = "leon";
        int size = 10;
        int offset = 0;

        // when
        List<User> users = repository.findByName(nonExistingName, size, offset);

        // then
        assertEquals(0, users.size());
    }

    @Test
    void shouldFindManyWhenNonUniqueName() {
        // given
        String nonUniqueName = "maria";
        int size = 10;
        int offset = 0;

        // when
        List<User> users = repository.findByName(nonUniqueName, size, offset);
        Executable checkSize = () -> assertEquals(2, users.size());
        Executable checkNameFisrtOccurence = () -> assertEquals("maria", users.get(0).getName());
        Executable checkNameSecondOccurence = () -> assertEquals("maria", users.get(1).getName());

        // then
        assertAll(checkSize, checkNameFisrtOccurence, checkNameSecondOccurence);
    }

    @Test
    void shouldFindAllUsersWhenThereAreRecords() {
        // given
        int size = 10;
        int offset = 0;
        int expected = 6;
        int actual;

        // when
        List<User> users = repository.findAll(size, offset);
        actual = users.size();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindNoUsersWhenThereAreNoRecords() {
        // given
        int size = 10;
        int offset = 0;
        int expected = 0;
        int actual;

        // when
        jdbcClient
                .sql("DELETE FROM `USER`;")
                .update();

        List<User> users = repository.findAll(size, offset);
        actual = users.size();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveUser() {
        // given
        User expectedUser = User.builder()
                .withId(7L)
                .withLogin("gaiusgamer987")
                .withPassword("J8i9jaZ2")
                .withEmail("gfonseca@test.com.br")
                .withName("gaius")
                .withAddress("rua das mangueiras, 5860")
                .withLastModified(LocalDate.of(2026, 04, 07))
                .withRole(Role.CUSTOMER)
                .build();

        // when
        Optional<User> actualUser = repository.save(expectedUser);

        // then
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void shouldNotUpdatePasswordWhenUpdatingUser() {
        // given
        User expectedUser = User.builder()
                .withId(1L)
                .withLogin("pedroXRE")
                .withPassword("Hsu76Op1")
                .withEmail("pedro@test.com.br")
                .withName("pedroso")
                .withAddress("rua das macieiras, 1998")
                .withLastModified(LocalDate.of(2026, 04, 19))
                .withRole(Role.OWNER)
                .build();

        // when
        Optional<User> actualUser = repository.update(expectedUser);

        // then
        assertEquals(expectedUser.getPassword(), actualUser.get().getPassword());
    }

    @Test
    void shouldUpdatePassword() {
        // given
        Long id = 1L;
        String password = "Ym69P0H12a";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        int expectedAffectedRows = 1;

        // when
        int actualAffectedRows = repository.updatePassword(id, hashedPassword);
        User updatedUser = repository.findById(id).get();

        // then
        assertAll(
            () -> assertEquals(expectedAffectedRows, actualAffectedRows),
            () -> assertTrue(BCrypt.checkpw(password, updatedUser.getPassword()))
        );
    }

    @Test
    void shouldDeleteUserWhenExistingId() {
        // given
        int expectedAffectedRows = 1;
        Long id = 1L;

        // when
        int actualAffectedRows = repository.delete(id);
        Optional<User> actualUser = repository.findById(id);

        // then
        assertAll(
            () -> assertEquals(expectedAffectedRows, actualAffectedRows),
            () -> assertTrue(actualUser.isEmpty())
        );
    }

    @Test
    void shouldCountEmail() {
        // given
        Long expected = 1L;
        String textField = "email";
        String email = "maria.joana@gmail.com";

        // when
        Long actual = repository.count(textField, email);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldCountWhenEmailField() {
        // given
        Long expected = 1L;
        String textField = "email";
        String value = "maria.joana@gmail.com";

        // when
        Long actual = repository.count(textField, value);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldCountWhenLoginField() {
        // given
        Long expected = 1L;
        String textField = "login";
        String value = "maria2020";

        // when
        Long actual = repository.count(textField, value);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowUnsupportedFieldExceptionWhenAnotherString() {
        // given
        String textField = "name";
        String value = "maria";

        // when
        Executable count = () -> repository.count(textField, value);

        // then
        assertThrows(UnsupportedFieldException.class, count);
    }
}
