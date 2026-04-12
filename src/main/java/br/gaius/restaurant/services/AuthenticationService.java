package br.gaius.restaurant.services;

import org.springframework.stereotype.Service;

import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.repositories.UserRepository;
import br.gaius.restaurant.repositories.UserRepositoryDataHandler;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepositoryDataHandler userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String login, String password) {
        User user = userRepository.findByLogin(login).get();
        
        if(!user.getPassword().equals(password)){
            return false;
        }
        
        return true;
    }

}
