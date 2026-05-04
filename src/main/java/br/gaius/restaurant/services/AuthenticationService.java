package br.gaius.restaurant.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.gaius.restaurant.dtos.AuthenticationRequestDTO;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.exceptions.InvalidPasswordException;
import br.gaius.restaurant.exceptions.UserNotFoundException;
import br.gaius.restaurant.repositories.UserRepository;

@Service
public class AuthenticationService {

    private UserRepository repository;

    public AuthenticationService(UserRepository repository) {
        this.repository = repository;
    }

    public void authenticate(AuthenticationRequestDTO auth) {
        User user = repository.findByLogin(auth.login())
                .orElseThrow(() -> new UserNotFoundException(auth.login()));

        if (!BCrypt.checkpw(auth.password(), user.getPassword())) {
            throw new InvalidPasswordException(auth.login(), auth.password());
        }
    }

}
