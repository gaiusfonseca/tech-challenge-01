package br.gaius.restaurant.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.exceptions.UnsupportedFieldException;

@Repository
public class UserRepositoryImplementation implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryImplementation(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sqlStatement = "SELECT * FROM `user` WHERE id = :id;";

        return jdbcClient
                .sql(sqlStatement)
                .param("id", id)
                .query(UserRowMapper::fromDB)
                .optional();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sqlStatement = "SELECT * FROM `user` WHERE login = :login";

        return jdbcClient
            .sql(sqlStatement)
            .param("login", login)
            .query(UserRowMapper::fromDB)
            .optional();
    }

    @Override
    public List<User> findByName(String name, int size, int offset) {
        String sqlStatement = "SELECT * FROM `user` WHERE `name` = :name LIMIT :size OFFSET :offset;";

        return jdbcClient
                .sql(sqlStatement)
                .param("name", name)
                .param("size", size)
                .param("offset", offset)
                .query(UserRowMapper::fromDB)
                .list();
    }

    @Override
    public List<User> findAll(int size, int offset) {
        String sqlStatement = "SELECT * FROM `user` LIMIT :size OFFSET :offset;";

        return jdbcClient
                .sql(sqlStatement)
                .param("size", size)
                .param("offset", offset)
                .query(UserRowMapper::fromDB)
                .list();
    }

    @Override
    public Optional<User> save(User user) {
        String sqlStatement = "INSERT INTO `user` (`login`, `password`, email, `name`, `address`, last_modified, `role`) "
                + "VALUES (:login, :password, :email, :name, :address, :lastModified, :role);";

        KeyHolder generatedKey = new GeneratedKeyHolder();

        jdbcClient
                .sql(sqlStatement)
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("email", user.getEmail())
                .param("name", user.getName())
                .param("address", user.getAddress())
                .param("lastModified", LocalDate.now())
                .param("role", user.getRole().name())
                .update(generatedKey);

        Long id = generatedKey.getKey().longValue();
        User updated = User.builder(user).withId(id).build();

        return Optional.of(updated);
    }

    @Override
    public Optional<User> update(User user) {
        String sqlStatement = "UPDATE `user` SET `login` = :login, email = :email, `name` = :name, "
                + "`address` = :address, last_modified = :lastModified, `role` = :role WHERE id = :id;";

        jdbcClient
                .sql(sqlStatement)
                .param("login", user.getLogin())
                .param("email", user.getEmail())
                .param("name", user.getName())
                .param("address", user.getAddress())
                .param("lastModified", LocalDate.now())
                .param("role", user.getRole().name())
                .param("id", user.getId())
                .update();

        return Optional.of(user);
    }

    @Override
    public int updatePassword(Long id, String hashedPassword) {
        String sqlStatement = "UPDATE `user` SET `password` = :password, last_modified = :lastModified WHERE id = :id;";

        return jdbcClient
                .sql(sqlStatement)
                .param("password", hashedPassword)
                .param("lastModified", LocalDate.now())
                .param("id", id)
                .update();
    }

    @Override
    public int delete(Long id) {
        String sqlStatement = "DELETE FROM `user` WHERE id = :id;";

        return jdbcClient
                .sql(sqlStatement)
                .param("id", id)
                .update();
    }

    @Override
    public Long count(String textField, String value) {

        if(!textField.equals("email") && !textField.equals("login")) {
            throw new UnsupportedFieldException(textField);
        }

        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM `user` WHERE ");
        sb.append(textField);
        sb.append(" = :value;");
        String sqlStatement = sb.toString();

        return jdbcClient
                .sql(sqlStatement)
                .param("value", value)
                .query(Long.class)
                .single();
    }
}
