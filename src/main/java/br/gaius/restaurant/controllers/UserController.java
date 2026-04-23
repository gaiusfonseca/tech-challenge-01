package br.gaius.restaurant.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.services.UserService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping("/restaurants/v1")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> findById(@PathVariable Long id) {
        logger.info("GET /restaurants/v1/{id}");

        Optional<User> user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findByName(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size) {
        String logMessage = String.format("GET /restaurants/v1/users/%s?page=%d?size=%d", name, page, size);
        logger.info(logMessage);

        List<User> users = userService.findByName(name, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll(
            @RequestParam int page,
            @RequestParam int size) {
        String logMessage = String.format("GET /restaurants/v1/users?page=%s&size=%s", page, size);
        logger.info(logMessage);

        List<User> users = userService.findAll(page, size);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<Void> save(@RequestBody CreateUserDTO newUser) {
        logger.info("POST /restaurants/v1/users");

        User userEntity = User.fromDTO(newUser);
        userService.save(userEntity);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody CreateUserDTO existingUser) {
        logger.info("PUT /restaurants/v1");

        User userEntity = User.fromDTO(existingUser);
        userService.update(id, userEntity);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/passwords/{id}")
    public ResponseEntity<Void> setPassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordDTO password) {
        String logMessage = String.format("PATCH /restaurants/v1/passowrds/%d", id);
        logger.info(logMessage);

        userService.setPassword(id, password);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String logMessage = String.format("DELETE /restaurants/v1//users/%d", id);
        logger.info(logMessage);

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
