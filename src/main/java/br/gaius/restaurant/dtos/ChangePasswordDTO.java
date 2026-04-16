package br.gaius.restaurant.dtos;

public record ChangePasswordDTO(Long id, String oldPassword, String newPassword) {

}
