package br.gaius.restaurant.dtos;

public record UpdateUserDTO(Long id, String login, String email, String name, String address) {

}
