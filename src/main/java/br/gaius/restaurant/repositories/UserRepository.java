package br.gaius.restaurant.repositories;

import java.util.List;
import java.util.Optional;

import br.gaius.restaurant.entities.User;

public interface UserRepository {

    public Optional<User> findById(Long id);

    public List<User> findByName(String name, int size, int offset);

    public List<User> findAll(int size, int offset);

    public int save(User user);

    public int update(User user);

    public int updatePassword(String hashedPassword);

    public int delete(Long id);

    public int count();

}
