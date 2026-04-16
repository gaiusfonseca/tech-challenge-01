package br.gaius.restaurant.entities;

import br.gaius.restaurant.dtos.CreateUserDTO;

public interface UserFactory {

    public User of(CreateUserDTO createUserDTO);
}
