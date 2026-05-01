package br.gaius.restaurant.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

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

import br.gaius.restaurant.entities.Role;
import br.gaius.restaurant.entities.User;
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
        JacksonTester<User> jacksonTester;

        @Test
        void shouldFindUserByIdWhenUserExists() throws Exception {
                // given
                Long id = 1L;

                User user = User.builder()
                                .withId(id)
                                .withLogin("pedro321")
                                .withEmail("pedro@gmail.com")
                                .withName("pedro")
                                .withAddress("rua das acácias, 12")
                                .withLastModified(LocalDate.of(2026, 04, 01))
                                .withRole(Role.CUSTOMER)
                                .build();

                when(service.findById(id)).thenReturn(Optional.of(user));

                // when
                MvcTestResult result = mockMvcTester.get()
                                .uri(Routes.USER_RESOURCE + Routes.WITH_ID.replace("{id}", id.toString())).exchange();

                // then
                assertThat(result)
                                .hasStatusOk()
                                .hasContentType(MediaType.APPLICATION_JSON)
                                .bodyJson().isEqualTo(jacksonTester.write(user).getJson());
        }

        @Test
        void shouldReturnEmptyJsonWhenUserDoesNotExist() {
                // given
                Long id = 20L;

                when(service.findById(id)).thenReturn(Optional.ofNullable(null));

                // when
                MvcTestResult result = mockMvcTester.get()
                                .uri(Routes.USER_RESOURCE + Routes.WITH_ID.replace("{id}", id.toString())).exchange();

                // then
                assertThat(result)
                                .hasStatusOk()
                                .hasContentType(MediaType.APPLICATION_JSON)
                                .bodyJson().extracting(content -> content.getJson()).isEqualTo("null");

                verify(service).findById(anyLong());
        }

        /* @Test
        void shouldSaveUser() throws Exception {
                // given
                Long id = 7L;
                String login = "mugiwaraLuffy";
                String password = "secret123";
                String email = "mugiwara.luffy@test.com.br";
                String name = "luffy";
                String address = "rua dos rei pirata, 7";
                LocalDate lastModified = LocalDate.now();
                Role role = Role.CUSTOMER;

                CreateUserDTO createDto = new CreateUserDTO(login, password, email, name, address, role);
                UserResponseDTO responseDto = new UserResponseDTO(id, login, email, name, address, lastModified, role);

                when(service.save(createDto)).thenReturn(responseDto);

                // when
                MvcTestResult result = mockMvcTester.post()
                .uri(Routes.USER_RESOURCE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(createDto).getJson())
                .exchange();

                
                // then
                assertThat(result)
                        .hasStatus(HttpStatus.CREATED)
                        .hasForwardedUrl(Routes.USER_RESOURCE + id);

                verify(service).save(createDto);

        } */

        @Test
        void shouldReturnStatusOKWhenDeleteUser() {
                // given
                Long id = 1L;

                // when
                MvcTestResult result = mockMvcTester.delete()
                                .uri(Routes.USER_RESOURCE + Routes.WITH_ID.replace("{id}", id.toString())).exchange();

                // then
                assertThat(result)
                                .hasStatus(HttpStatus.NO_CONTENT);

                verify(service).delete(anyLong());
        }

}
