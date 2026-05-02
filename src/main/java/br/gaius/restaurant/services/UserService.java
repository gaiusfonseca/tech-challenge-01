package br.gaius.restaurant.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.gaius.restaurant.dtos.ChangePasswordDTO;
import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.dtos.UserResponseDTO;
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

    public Optional<UserResponseDTO> findById(Long id) {
        Optional<User> possibleUser = repository.findById(id);
        UserResponseDTO dto = mapper.to(possibleUser.orElse(null));
        return Optional.ofNullable(dto);
    }

    public List<UserResponseDTO> findByName(String name, int page, int size) {
        if (page < 1){
            page = 1;
        }

        if (size < 1){
            size = 10;
        }

        int offset = getOffset(page, size);
        List<User> users = repository.findByName(name, size, offset);
        List<UserResponseDTO> dtos = users.stream().map(user -> mapper.to(user)).toList();

        return dtos;
    }

    public List<UserResponseDTO> findByName(String name) {
        int page = 1;
        int size = 10;
        return findByName(name, page, size);
    }

    public List<UserResponseDTO> findAll(int page, int size) {
        if (page < 1){
            page = 1;
        }

        if (size < 1){
            size = 10;
        }

        int offset = getOffset(page, size);
        List<User> users = repository.findAll(size, offset);
        List<UserResponseDTO> dtos = users.stream().map(user -> mapper.to(user)).toList();

        return dtos;
    }

    public List<UserResponseDTO> findAll() {
        int page = 1;
        int size = 10;
        return findAll(page, size);
    }

    public UserResponseDTO save(CreateUserDTO createDTO) {
        User user = mapper.from(createDTO);

        if(isDuplicatedEmail(createDTO.email())){
            throw new DuplicatedEmailException(createDTO.email());
        }

        if(isDuplicatedLogin(createDTO.login())){
            throw new DuplicatedLoginException(createDTO.login());
        }

        user = repository.save(user).orElseThrow();
        UserResponseDTO responseDTO = mapper.to(user);

        return responseDTO;
    }

    public UserResponseDTO update(Long id, UpdateUserDTO updateDTO) {
        User user = mapper.from(id, updateDTO);

        if(isDuplicatedEmail(updateDTO.email())){
            throw new DuplicatedEmailException(updateDTO.email());
        }

        if(isDuplicatedLogin(updateDTO.login())){
            throw new DuplicatedLoginException(updateDTO.login());
        }

        user = repository.update(user).orElseThrow();
        UserResponseDTO responseDTO = mapper.to(user);

        return responseDTO;
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
