package br.gaius.restaurant.controllers;

import java.util.List;
import java.util.Optional;

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

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.services.UserService;

@RestController
@RequestMapping(Routes.USER_RESOURCE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(Routes.WITH_ID)
    public ResponseEntity<Optional<User>> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(Routes.WITH_NAME)
    public ResponseEntity<List<User>> findByName(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size) {

        List<User> users = userService.findByName(name, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping(Routes.WITH_PAGING)
    public ResponseEntity<List<User>> findAll(
            @RequestParam int page,
            @RequestParam int size) {

        List<User> users = userService.findAll(page, size);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateUserDTO dto) {
        userService.save(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(Routes.WITH_ID)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        userService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(Routes.PWD_RESOURCE)
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
