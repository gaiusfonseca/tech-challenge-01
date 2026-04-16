package br.gaius.restaurant.entities;

import br.gaius.restaurant.dtos.CreateUserDTO;

public class CustomerUserFactory implements UserFactory{

    @Override
    public User of(CreateUserDTO createUserDTO) {
        return new CustomerUser(createUserDTO.login(), createUserDTO.password(), createUserDTO.email(), createUserDTO.name(), createUserDTO.address());
    }
}
