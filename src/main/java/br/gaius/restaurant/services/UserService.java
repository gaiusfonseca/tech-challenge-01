package br.gaius.restaurant.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.entities.UserMapper;
import br.gaius.restaurant.exceptions.DuplicatedEmailException;
import br.gaius.restaurant.exceptions.DuplicatedLoginException;
import br.gaius.restaurant.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public List<User> findByName(String name, int page, int size) {
        if (page < 1){
            page = 1;
        }

        if (size < 1){
            size = 10;
        }

        int offset = getOffset(page, size);
        return repository.findByName(name, size, offset);
    }

    public List<User> findByName(String name) {
        int page = 1;
        int size = 10;
        return findByName(name, page, size);
    }

    public List<User> findAll(int page, int size) {
        if (page < 1){
            page = 1;
        }

        if (size < 1){
            size = 10;
        }

        int offset = getOffset(page, size);
        return repository.findAll(size, offset);
    }

    public List<User> findAll() {
        int page = 1;
        int size = 10;
        return findAll(page, size);
    }

    public User save(CreateUserDTO dto) {
        User user = mapper.from(dto);

        if(isDuplicatedEmail(dto.email())){
            throw new DuplicatedEmailException(dto.email());
        }

        if(isDuplicatedLogin(dto.login())){
            throw new DuplicatedLoginException(dto.login());
        }

        return repository.save(user).orElseThrow();
    }

    public User update(Long id, UpdateUserDTO dto) {
        User user = mapper.from(id, dto);

        if(isDuplicatedEmail(dto.email())){
            throw new DuplicatedEmailException(dto.email());
        }

        if(isDuplicatedLogin(dto.login())){
            throw new DuplicatedLoginException(dto.login());
        }

        return repository.update(user).orElseThrow();
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public void updatePassword(Long id, ChangePasswordDTO dto) {
        User user = repository.findById(id).orElseThrow();
        String hashedPassword = BCrypt.hashpw(dto.newPassword(), BCrypt.gensalt());
        repository.updatePassword(user.getId(), hashedPassword);
    }

    private int getOffset(int page, int size) {
        return (page - 1) * size;
    }

    private boolean isDuplicatedEmail(String email){
        if (repository.count("email", email) > 0) {
            return true;
        }

        return false;
    }

    private boolean isDuplicatedLogin(String login){
        if (repository.count("login", login) > 0) {
            return true;
        }

        return false;
    }
}
