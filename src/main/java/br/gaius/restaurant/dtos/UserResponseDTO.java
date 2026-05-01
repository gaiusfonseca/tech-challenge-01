package br.gaius.restaurant.dtos;

import java.time.LocalDate;

import br.gaius.restaurant.entities.Role;
import br.gaius.restaurant.entities.User;

public record UserResponseDTO(Long id, String login, String email, String name, String address, LocalDate lastModified,
        Role role) {

    public UserResponseDTO(User user){
        this(user.getId(), user.getLogin(), user.getEmail(), user.getName(), user.getAddress(), user.getLastModified(), user.getRole());
    }
}
