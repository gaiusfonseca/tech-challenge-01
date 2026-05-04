package br.gaius.restaurant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gaius.restaurant.dtos.AuthenticationRequestDTO;
import br.gaius.restaurant.services.AuthenticationService;


@RestController
@RequestMapping(Routes.BASE_URL + Routes.AUTHENTICATIONS)
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody AuthenticationRequestDTO auth) {
        service.authenticate(auth);
        return ResponseEntity.noContent().build();
    }
    
}
