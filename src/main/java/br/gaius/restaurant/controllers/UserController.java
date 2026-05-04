package br.gaius.restaurant.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.dtos.UserResponseDTO;
import br.gaius.restaurant.services.UserService;

@RestController
@RequestMapping(Routes.USERS)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(Routes.WITH_ID)
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO dto = userService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam int page,
            @RequestParam int size) {

        if(name == null || name.isBlank()){
            List<UserResponseDTO> dtos = userService.findAll(page, size);
            return ResponseEntity.ok(dtos);
        }

        List<UserResponseDTO> dtos = userService.findByName(name, page, size);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateUserDTO createDTO) {
        UserResponseDTO responseDTO = userService.save(createDTO);
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/v1/users/{id}").build(responseDTO.id());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(Routes.WITH_ID)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        userService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(Routes.PASSWORDS)
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordDTO dto) {
        userService.updatePassword(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(Routes.WITH_ID)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
