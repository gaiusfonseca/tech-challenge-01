package br.gaius.restaurant.repositories;

import java.util.List;
import java.util.Optional;

import br.gaius.restaurant.entities.User;

public interface UserRepository {

    public Optional<User> findById(String email);

    public List<User> findAll(int size, int offset);

    public int save(User user);

    public int update(String email, User user);

    public int setPassword(User user);

    public int delete(String email);

    public int count();
}
