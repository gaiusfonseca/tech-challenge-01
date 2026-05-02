package br.gaius.restaurant.dtos;

import java.time.LocalDate;

import br.gaius.restaurant.entities.Role;

public record UserResponseDTO(Long id, String login, String email, String name, String address, LocalDate lastModified,
        Role role) {
}
