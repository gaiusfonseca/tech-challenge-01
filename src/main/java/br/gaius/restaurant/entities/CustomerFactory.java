package br.gaius.restaurant.entities;

import java.time.LocalDate;

import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;

public class CustomerFactory extends UserFactory {

    @Override
    public User ofNew(CreateUserDTO userDTO) {
        // TODO validar userType?
        // TODO validar id?
        return new CustomerUser(
            null, userDTO.login(), userDTO.password(), userDTO.email(), userDTO.name(), userDTO.address(), LocalDate.now()
        );
    }

    @Override
    public User ofExisting(UpdateUserDTO userDTO) {
        // TODO validar userType?
        // TODO validar id?
        return new CustomerUser(
            userDTO.id(), userDTO.login(), null, userDTO.email(), userDTO.name(), userDTO.address(), LocalDate.now()
        );
    }

}
