package br.gaius.restaurant.dtos;

import br.gaius.restaurant.entities.Role;

public record CreateUserDTO(String login, String password, String email, String name, String address, Role role) {

}
