package br.gaius.restaurant.entities;

import br.gaius.restaurant.dtos.CreateUserDTO;

public class OwnerUserFactory implements UserFactory {

    @Override
    public User of(CreateUserDTO createUserDTO) {
        return new OwnerUser(createUserDTO.login(), createUserDTO.password(), createUserDTO.email(), createUserDTO.name(), createUserDTO.address());
    }

}
