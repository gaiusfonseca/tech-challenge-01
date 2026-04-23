package br.gaius.restaurant.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.entities.UserFactory;
import br.gaius.restaurant.exceptions.DuplicatedEmailException;
import br.gaius.restaurant.exceptions.InvalidPasswordException;
import br.gaius.restaurant.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findByName(String name, int page, int size){
        if(page < 1) page = 1;
        if(size < 1) size = 10;
        int offset = getOffset(page, size);
        return userRepository.findByName(name, size, offset);
    }

    public List<User> findByName(String name){
        int page = 1;
        int size = 10;
        return findByName(name, page, size);
    }

    public List<User> findAll(int page, int size) {
        if(page < 1) page = 1;
        if(size < 1) size = 10;
        int offset = getOffset(page, size);
        return userRepository.findAll(size, offset);
    }

    public List<User> findAll() {
        int page = 1;
        int size = 10;
        return findAll(page, size);
    }

    public User save(CreateUserDTO requestBody){
        UserFactory factory = UserFactory.of(requestBody.userType());
        User userEntity = factory.fromCreateRequest(requestBody);
        return userRepository.save(userEntity).orElseThrow();
    }

    public User update(UpdateUserDTO requestBody){
        UserFactory factory = UserFactory.of(requestBody.userType());
        User userEntity = factory.fromUpdateRequest(requestBody);
        if(userRepository.count(requestBody.email()) > 0) throw new DuplicatedEmailException(userEntity.getEmail());
        return userRepository.update(userEntity).orElseThrow();
    }

    public void delete(Long id){
        userRepository.delete(id);
    }

    public Long count(String email){
        return userRepository.count(email);
    }

    public void changePassword(Long id, ChangePasswordDTO requestBody) {
        User user = userRepository.findById(id).orElseThrow();
        
        if(user.passwordMatches(requestBody.oldPassword())){
            userRepository.updatePassword(id, requestBody.newPassword(), LocalDate.now());
            return;
        }

        throw new InvalidPasswordException(id, requestBody.oldPassword());
    }

    private static int getOffset(int page, int size){
        return (page - 1) * size;
    }
}
