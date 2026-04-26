package br.gaius.restaurant.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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

    public void authenticate(User candidate) {
        User user = repository.findByLogin(candidate.getLogin())
                .orElseThrow(() -> new UserNotFoundException(candidate.getLogin()));

        if (BCrypt.checkpw(candidate.getPassword(), user.getPassword())) {
            return;
        }

        throw new InvalidPasswordException(candidate.getLogin(), candidate.getPassword());
    }

}
