package br.gaius.restaurant.services;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import br.gaius.restaurant.repositories.UserRepository;

@ActiveProfiles("test")
public class UserServiceTest {

    private UserRepository repository;
    private UserService service;

    @BeforeEach
    void setup() {
        repository = mock(UserRepository.class);
        service = new UserService(repository);
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
    void shouldSaveWhenNoDuplicateEmail() {
        // given

        // when

        // then
        fail("Not yet implemented");
    }

    @Test
    void shouldThrowDuplicatedEmailExceptionWhenSaveDuplicatedEmail() {
        // given

        // when

        // then
        fail("Not yet implemented");
    }

    @Test
    void shouldUpdateUserWhenEmailIsNotDuplicated() {
        // given

        // when

        // then
        fail("Not yet implemented");
    }

    @Test
    void shouldThrowDuplicatedEmailExceptionWhenUpdateEmailDuplicated() {
        // given

        // when

        // then
        fail("Not yet implemented");
    }

    @Test
    void shouldDeleteWhenDelete(){
        // given
        Long id = 1L;
        
        // when
        service.delete(id);

        // then
        verify(repository).delete(id);
    }

    @Test
    void shouldCountWhenCount(){
        // given
        String email = "something@test.com.br";
        
        
        // when
        service.count(email);

        // then
        verify(repository).count(email);
    }
}
