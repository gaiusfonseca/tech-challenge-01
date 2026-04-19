package br.gaius.restaurant.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.entities.UserBuilder;
import br.gaius.restaurant.services.UserService;

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
        // 'pedro321', 'X321kHEz', 'pedro@gmail.com', 'pedro', 'rua das acácias, 12',
        // '2026-04-01', 'customer'
        Long existingId = 1L;
        UserBuilder builder = new UserBuilder();
        Optional<User> expected = Optional.of(builder
                .withId(existingId)
                .withLogin("pedro321")
                .withPassword("X321kHEz")
                .withEmail("pedro@gmail.com")
                .withName("pedro")
                .withAddress("rua das acácias, 12")
                .withLastModified(LocalDate.of(2026, 04, 01))
                .withUserType("customer")
                .build());
        // when
        Optional<User> actual = repository.findById(existingId);

        // then
        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.get().toString(), actual.get().toString()));
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
    void shouldFindOneWhenUniqueName() {
        // given
        String uniqueName = "pedro";
        int size = 10;
        int offset = 0;

        // when
        List<User> users = repository.findByName(uniqueName, size, offset);
        Executable checkSize = () -> assertEquals(1, users.size());
        Executable checkName = () -> assertEquals("pedro", users.get(0).getName());

        // then
        assertAll(checkSize, checkName);
    }

    @Test
    void shouldReturnEmptyListWhenNonExistingName() {
        // given
        String nonExistingName = "leon";
        int size = 10;
        int offset = 0;

        // when
        List<User> users = repository.findByName(nonExistingName, size, offset);
        Executable checkSize = () -> assertEquals(0, users.size());

        // then
        assertAll(checkSize);
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
    void shouldSaveUserWhenValidDataAndUser() {
        // given
        UserBuilder builder = new UserBuilder();
        User expectedUser = builder
                .withId(7L)
                .withLogin("gaiusgamer987")
                .withPassword("J8i9jaZ2")
                .withEmail("gfonseca@test.com.br")
                .withName("gaius")
                .withAddress("rua das mangueiras, 5860")
                .withLastModified(LocalDate.of(2026, 04, 07))
                .withUserType("customer")
                .build();

        // when
        Optional<User> actualUser = repository.save(expectedUser);

        // then
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void shouldNotUpdatePasswordWhenUpdatingUser() {
        // given
        UserBuilder builder = new UserBuilder();
        User expectedUser = builder
                .withId(1L)
                .withLogin("pedroXRE")
                .withPassword("Hsu76Op1")
                .withEmail("pedro@test.com.br")
                .withName("pedroso")
                .withAddress("rua das macieiras, 1998")
                .withLastModified(LocalDate.of(2026, 04, 19))
                .withUserType("owner")
                .build();

        // when
        Optional<User> actualUser = repository.update(expectedUser);

        // then
        Executable checkUser = () -> assertEquals(expectedUser, actualUser.get());
        assertAll(checkUser);
    }

    @Test
    void shouldUpdatePasswordWhenUpdatePasswordMethod() {
        // given
        int expectedAffectedRows = 1;
        Long id = 1L;
        String hashedPassword = "Hsu76Op1";
        LocalDate lastModified = LocalDate.now();
        String userType = "customer";

        UserBuilder builder = new UserBuilder();
        User expectedUser = builder
                .withId(id)
                .withPassword(hashedPassword)
                .withLastModified(lastModified)
                .withUserType(userType)
                .build();

        // when
        int actualAffectedRows = repository.updatePassword(id, hashedPassword, lastModified);
        User actualUser = repository.findById(id).get();

        // then
        Executable checkAffectedRows = () -> assertEquals(expectedAffectedRows, actualAffectedRows);
        Executable checkChanges = () -> assertEquals(expectedUser, actualUser);
        assertAll(checkAffectedRows, checkChanges);
    }

    @Test
    void shouldDeleteUserWhenExistingId(){
        //given
        int expectedAffectedRows = 1;
        Long id = 1L;

        //when
        int actualAffectedRows = repository.delete(id);
        Optional<User> actualUser = repository.findById(id);

        //then
        Executable checkAffectedRows = () -> assertEquals(expectedAffectedRows, actualAffectedRows);
        Executable checkEmptyUser = () -> assertTrue(actualUser.isEmpty());
        assertAll(checkAffectedRows, checkEmptyUser);
    }

    @Test
    void shouldReturnTheCountOfRecords() {
        //given
        Long expected = 6L;

        //when
        Long actual = repository.count();

        //then
        assertEquals(expected, actual);
    }
}
