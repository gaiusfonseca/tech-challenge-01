package br.gaius.restaurant.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import br.gaius.restaurant.entities.User;

@Repository
public class UserRepositoryDataHandler implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryDataHandler(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<User> findById(String email) {
        String sqlStatement = "SELECT * FROM `user` WHERE email = :email;";

        return jdbcClient
                .sql(sqlStatement)
                .param("email", email)
                .query(User.class)
                .optional();
    }

    @Override
    public List<User> findAll(int size, int offset) {
        String sqlStatement = "SELECT * FROM `user` LIMIT :size OFFSET :offset;";

        return jdbcClient
                .sql(sqlStatement)
                .param("size", size)
                .param("offset", offset)
                .query(User.class)
                .list();
    }

    @Override
    public int save(User user) {
        String sqlStatement = "INSERT INTO `user` (email, `name`, `login`, `password`, `address`, last_modified) " +
                "VALUES (:email, :name, :login, :password, :address, :lastModified);";

        return jdbcClient
                .sql(sqlStatement)
                .param("email", user.getEmail())
                .param("name", user.getName())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("address", user.getAddress())
                .param("lastModified", user.getLastModified())
                .update();
    }

    @Override
    public int update(String email, User user) {
        String sqlStatement = "UPDATE `user` SET `name` = :name, `login` = :login, `address` = :address, last_modified = :lastModified WHERE email = :email;";

        return jdbcClient
                .sql(sqlStatement)
                .param("name", user.getName())
                .param("login", user.getLogin())
                .param("address", user.getAddress())
                .param("lastModified", user.getLastModified())
                .param("email", email)
                .update();
    }

    @Override
    public int setPassword(User user) {
        String sqlStatement = "UPDATE `user` SET `password` = :password, last_modified = :lastModified WHERE email = :email;";

        return jdbcClient
            .sql(sqlStatement)
            .param("password", user.getPassword())
            .param("lastModified", user.getLastModified())
            .param("email", user.getEmail())
            .update();
    }

    @Override
    public int delete(String email) {
        String sqlStatement = "DELETE FROM `user` WHERE email = :email;";

        return jdbcClient
                .sql(sqlStatement)
                .param("email", email)
                .update();
    }

    @Override
    public int count() {
        String sqlStatement = "SELECT COUNT(email) FROM `user`";

        return jdbcClient
                .sql(sqlStatement)
                .update();
    }
}
