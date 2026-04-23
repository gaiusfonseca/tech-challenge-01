package br.gaius.restaurant.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import br.gaius.restaurant.entities.User;
import br.gaius.restaurant.entities.UserFactory;

@Repository
public class UserRepositoryImplementation implements UserRepository {

    private final JdbcClient jdbcClient;
    private final RowMapper<User> userMapper = (rs, index) -> {
        UserFactory factory = UserFactory.of(rs.getString("user_type"));
        return factory.fromReadDB(rs);
    };

    public UserRepositoryImplementation(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sqlStatement = "SELECT * FROM `user` WHERE id = :id;";

        return jdbcClient
                .sql(sqlStatement)
                .param("id", id)
                .query(userMapper)
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
                .query(userMapper)
                .list();
    }

    @Override
    public List<User> findAll(int size, int offset) {
        String sqlStatement = "SELECT * FROM `user` LIMIT :size OFFSET :offset;";

        return jdbcClient
                .sql(sqlStatement)
                .param("size", size)
                .param("offset", offset)
                .query(userMapper)
                .list();
    }

    @Override
    public Optional<User> save(User user) {
        String sqlStatement = "INSERT INTO `user` (`login`, `password`, email, `name`, `address`, last_modified, user_type) "
                + "VALUES (:login, :password, :email, :name, :address, :lastModified, :userType);";

        KeyHolder generatedKey = new GeneratedKeyHolder();
        UserFactory factory = UserFactory.of(user.getType());

        jdbcClient
                .sql(sqlStatement)
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("email", user.getEmail())
                .param("name", user.getName())
                .param("address", user.getAddress())
                .param("lastModified", user.getLastModified())
                .param("userType", user.getType())
                .update(generatedKey);

        return Optional.of(factory.fromSaveDB(generatedKey.getKey().longValue(), user));
    }

    @Override
    public Optional<User> update(User user) {
        String sqlStatement = "UPDATE `user` SET `login` = :login, email = :email, `name` = :name, "
                + "`address` = :address, last_modified = :lastModified, user_type = :userType WHERE id = :id;";

        jdbcClient
                .sql(sqlStatement)
                .param("login", user.getLogin())
                .param("email", user.getEmail())
                .param("name", user.getName())
                .param("address", user.getAddress())
                .param("lastModified", user.getLastModified())
                .param("userType", user.getType())
                .param("id", user.getId())
                .update();

        return Optional.of(user);
    }

    @Override
    public int updatePassword(Long id, String hashedPassword, LocalDate lastModified) {
        String sqlStatement = "UPDATE `user` SET `password` = :password, last_modified = :lastModified WHERE id = :id;";

        return jdbcClient
                .sql(sqlStatement)
                .param("password", hashedPassword)
                .param("lastModified", lastModified)
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
    public Long count(String email) {
        String sqlStatement = "SELECT COUNT(*) FROM `user` WHERE email = :email;";

        return jdbcClient
                .sql(sqlStatement)
                .param("email", email)
                .query(Long.class)
                .single();
    }
}
