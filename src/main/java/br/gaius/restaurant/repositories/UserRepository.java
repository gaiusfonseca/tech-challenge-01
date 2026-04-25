package br.gaius.restaurant.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import br.gaius.restaurant.entities.User;

public interface UserRepository {

    public Optional<User> findById(Long id);

    public Optional<User> findByLogin(String login);
    
    public List<User> findByName(String name, int size, int offset);

    public List<User> findAll(int size, int offset);

    public Optional<User> save(User user);

    public Optional<User> update(User user);

    public int updatePassword(Long id, String hashedPassword);

    public int delete(Long id);

    public Long count(String email);

}
