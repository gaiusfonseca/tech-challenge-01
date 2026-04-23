package br.gaius.restaurant.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;

public class CustomerFactory extends UserFactory {

    @Override
    public User fromCreateRequest(CreateUserDTO createDTO) {
        return new CustomerUser(
                null, createDTO.login(), createDTO.password(), createDTO.email(), createDTO.name(), createDTO.address(),
                LocalDate.now());
    }

    @Override
    public User fromUpdateRequest(UpdateUserDTO updateDTO) {
        return new CustomerUser(
                updateDTO.id(), updateDTO.login(), null, updateDTO.email(), updateDTO.name(), updateDTO.address(),
                LocalDate.now());
    }

    @Override
    public User fromChangePasswordRequest(Long id, String newPassword) {
        return new CustomerUser(
                id, null, newPassword, null, null, null, LocalDate.now());
    }

    @Override
    public User fromReadDB(ResultSet rs) throws SQLException {
        return new CustomerUser(rs.getLong("id"), rs.getString("login"), rs.getString("password"),
                rs.getString("email"), rs.getString("name"), rs.getString("address"),
                rs.getDate("last_modified").toLocalDate());
    }

    @Override
    public User fromSaveDB(Long generatedKey, User user) {
        return new CustomerUser(generatedKey, user.getLogin(), user.getPassword(), user.getEmail(), user.getName(),
                user.getAddress(), user.getLastModified());
    }

    @Override
    public User fromUpdateDB(User user) {
        return new CustomerUser(user.getId(), user.getLogin(), user.getPassword(), user.getEmail(), user.getName(),
                user.getAddress(), user.getLastModified());
    }

}
