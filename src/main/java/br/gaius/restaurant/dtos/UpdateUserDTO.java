package br.gaius.restaurant.dtos;

import br.gaius.restaurant.entities.Role;

public record UpdateUserDTO(Long id, String login, String email, String name, String address, Role role) {

}
