package br.gaius.restaurant.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.services.UserService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{email}")
    public ResponseEntity<Optional<User>> findById(@PathVariable String email) {
        logger.info("GET /restaurants/v1/{email}");

        Optional<User> user = userService.findById(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(
            @RequestParam int page,
            @RequestParam int size) {
        String logMessage = String.format("GET /restaurants/v1?page=%s&size=%s", page, size);
        logger.info(logMessage);

        List<User> users = userService.findAll(page, size);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody User user) {
        logger.info("POST /restaurants/v1");

        userService.save(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> update(
            @PathVariable String email,
            @RequestBody User user) {
        logger.info("PUT /restaurants/v1");

        userService.update(email, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email){
        String logMessage = String.format("DELETE /restaurants/v1/%s", email);
        logger.info(logMessage);

        userService.delete(email);
        return ResponseEntity.noContent().build();
    }


}
