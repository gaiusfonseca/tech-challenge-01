package br.gaius.restaurant.dtos;

public record CreateUserDTO(String login, String password, String email, String name, String address, String userType) {

}
