package br.gaius.restaurant.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.gaius.restaurant.dtos.PasswordDTO;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.repositories.UserRepositoryDataHandler;

@Service
public class UserService {

    private final UserRepositoryDataHandler userRepository;

    public UserService(UserRepositoryDataHandler userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(String email) {
        return userRepository.findById(email);
    }

    public List<User> findAll(int page, int size) {
        int offset = (page - 1) * size;
        return userRepository.findAll(size, offset);
    }

    public void save(User user){
        int affectedRows = userRepository.save(user);
        if(affectedRows == 0){
            String exceptionMessage = String.format("erro ao salvar o usuário %s.", user.getEmail());
            throw new RuntimeException(exceptionMessage);
        }
    }

    public void update(String email, User user){
        int affectedRows = userRepository.update(email, user);
        if(affectedRows == 0){
            String exceptionMessage = String.format("o usuário com email %s não foi encontrado.", user.getEmail());
            throw new RuntimeException(exceptionMessage);
        }
    }

    public void delete(String email){
        int affectedRows = userRepository.delete(email);
        if(affectedRows == 0){
            String exceptionMessage = String.format("o usuário com email %s não foi encontrado.", email);
            throw new RuntimeException(exceptionMessage);
        }
    }

    public int count(){
        return userRepository.count();
    }

    public void setPassword(String email, PasswordDTO password) {
        Optional<User> result = userRepository.findById(email);
        User user = result.orElseThrow();
        
        user.setPassword(password.oldPassword(), password.newPassword());
        userRepository.setPassword(user);
    }

}
