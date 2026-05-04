package br.gaius.restaurant.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.web.util.UriComponentsBuilder;

import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UserResponseDTO;
import br.gaius.restaurant.entities.UserFixture;
import br.gaius.restaurant.exceptions.DuplicatedEmailException;
import br.gaius.restaurant.exceptions.DuplicatedLoginException;
import br.gaius.restaurant.exceptions.InvalidPaginationParameterException;
import br.gaius.restaurant.exceptions.UserNotFoundException;
import br.gaius.restaurant.services.UserService;

/* 
@SpringBootTest
Não sobe um servidor, mas carrega todo o contexto da aplicação, 
services e respositories são carregadas de fato e não meros mocks.
Recomendada para testes de integração.
 */

/* 
@WebMvcTest
Carrega apenas a camada web: Controllers, Filtes, Advices e Handlers.
Todo o resto são apenas Mocks. Ideal para testes unitários
 */

@WebMvcTest(UserController.class)
@AutoConfigureJsonTesters
public class UserControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private UserService service;

    @Autowired
    JacksonTester<UserResponseDTO> responseJsonTester;

    @Autowired
    JacksonTester<CreateUserDTO> createJsonTester;

    @Test
    void shouldFindUserByIdWhenUserExists() throws Exception {
        // given
        Long id = 7L;
        UserResponseDTO dto = UserFixture.getResponseDTO();

        when(service.findById(id)).thenReturn(dto);

        // when
        MvcTestResult result = mockMvcTester.get()
                .uri(Routes.USERS + Routes.WITH_ID.replace("{id}", id.toString()))
                .exchange();

        // then
        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().extracting(jsonContent -> jsonContent.getJson())
                .isEqualTo(responseJsonTester.write(dto).getJson());

        verify(service).findById(id);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesntExist() {
        // given
        Long id = 99L;

        when(service.findById(id)).thenThrow(UserNotFoundException.class);

        // when
        MvcTestResult result = mockMvcTester.get()
                .uri(Routes.USERS + Routes.WITH_ID.replace("{id}", id.toString())).exchange();

        // then
        assertThat(result)
                .hasFailed()
                .hasStatus(HttpStatus.NOT_FOUND)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().extractingPath("$.title").isEqualTo("User not found");

        verify(service).findById(anyLong());
    }

    @Test
    void shouldFindAllWhenCorrectParams() {
        // given
        int page = 1;
        int size = 10;

        URI requestURL = UriComponentsBuilder.fromUriString(Routes.USERS)
            .queryParam("page", "{page}")
            .queryParam("size", "{size}")
            .encode()
            .buildAndExpand(Integer.toString(page), Integer.toString(size))
            .toUri();

        List<UserResponseDTO> users = List.of(UserFixture.getResponseDTO(), UserFixture.getResponseDTO());

        when(service.findAll(page, size)).thenReturn(users);

        // when
        MvcTestResult result = mockMvcTester.get().uri(requestURL).exchange();

        // then
        assertThat(result)
            .hasStatus(HttpStatus.OK)
            .hasContentType(MediaType.APPLICATION_JSON)
            .bodyJson().extractingPath("$").asArray().hasSize(2);
    }

    @Test
    void shouldThrowInvalidPaginationParamsWhenInvalidParams() {
        // given
        int page = -1;
        int size = -1;

        URI requestURL = UriComponentsBuilder.fromUriString(Routes.USERS)
            .queryParam("page", "{page}")
            .queryParam("size", "{size}")
            .encode()
            .buildAndExpand(Integer.toString(page), Integer.toString(size))
            .toUri();

        when(service.findAll(page, size)).thenThrow(InvalidPaginationParameterException.class);

        // when
        MvcTestResult result = mockMvcTester.get().uri(requestURL).exchange();

        // then
        assertThat(result)
            .hasFailed()
            .hasStatus(HttpStatus.BAD_REQUEST)
            .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
            .bodyJson().extractingPath("$.title").isEqualTo("Invalid Pagination Parameter Request");
    }

    @Test
    void shouldThrowMissingRequestParamsWhenNoParams() {
        // given
        URI requestURL = UriComponentsBuilder.fromUriString(Routes.USERS)
            .build()
            .toUri();

        // when
        MvcTestResult result = mockMvcTester.get().uri(requestURL).exchange();

        // then
        assertThat(result)
            .hasFailed()
            .hasStatus(HttpStatus.BAD_REQUEST)
            .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
            .bodyJson().extractingPath("$.title").isEqualTo("Missing Request Parameters");
    }

    @Test
    void shouldSaveUserWhenNoDuplicatedEmailOrLogin() throws Exception {
        // given
        Long id = 7L;
        CreateUserDTO createDTO = UserFixture.getCreateDTO();
        UserResponseDTO responseDTO = UserFixture.getResponseDTO();

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080" + Routes.USERS + Routes.WITH_ID)
                .build(id.toString());

        when(service.save(createDTO)).thenReturn(responseDTO);

        // when
        MvcTestResult result = mockMvcTester
                .post()
                .uri(Routes.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJsonTester.write(createDTO).getJson())
                .exchange();

        // then
        assertThat(result)
                .hasStatus(HttpStatus.CREATED)
                .hasHeader("location", uri.toString());

        verify(service).save(createDTO);
    }

    @Test
    void shouldThrowDuplicatedEmailExceptionWhenEmailIsDuplicated() throws Exception {
        // given
        CreateUserDTO createDTO = UserFixture.getCreateDTO();

        when(service.save(createDTO)).thenThrow(DuplicatedEmailException.class);

        // when
        MvcTestResult result = mockMvcTester
                .post()
                .uri(Routes.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJsonTester.write(createDTO).getJson())
                .exchange();

        // then
        assertThat(result)
                .hasFailed()
                .hasStatus(HttpStatus.CONFLICT)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().extractingPath("$.title").isEqualTo("Duplicated Email");

        verify(service).save(createDTO);
    }

    @Test
    void shouldThrowDuplicatedLoginExceptionWhenEmailIsDuplicated() throws Exception {
        // given
        CreateUserDTO createDTO = UserFixture.getCreateDTO();

        when(service.save(createDTO)).thenThrow(DuplicatedLoginException.class);

        // when
        MvcTestResult result = mockMvcTester
                .post()
                .uri(Routes.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJsonTester.write(createDTO).getJson())
                .exchange();

        // then
        assertThat(result)
                .hasFailed()
                .hasStatus(HttpStatus.CONFLICT)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().extractingPath("$.title").isEqualTo("Duplicated Login");

        verify(service).save(createDTO);
    }

    @Test
    void shouldReturnStatusOKWhenDeleteUser() {
        // given
        Long id = 1L;

        // when
        MvcTestResult result = mockMvcTester.delete()
                .uri(Routes.USERS + Routes.WITH_ID.replace("{id}", id.toString())).exchange();

        // then
        assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);

        verify(service).delete(anyLong());
    }

}
