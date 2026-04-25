package br.gaius.restaurant.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.gaius.restaurant.entities.Role;
import br.gaius.restaurant.entities.User;

public class UserRowMapper {

    static User fromDB(ResultSet rs, int Index) throws SQLException{
        return User.builder()
            .withId(rs.getLong("id"))
            .withLogin(rs.getString("login"))
            .withPassword(rs.getString("password"))
            .withEmail(rs.getString("email"))
            .withName(rs.getString("name"))
            .withAddress(rs.getString("address"))
            .withLastModified(rs.getDate("last_modified").toLocalDate())
            .withRole(Role.valueOf(rs.getString("role")))
            .build();
        }

}
