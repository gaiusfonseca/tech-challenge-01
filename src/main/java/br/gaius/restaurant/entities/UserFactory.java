package br.gaius.restaurant.entities;

import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.exceptions.NoSuchTypeException;

public abstract class UserFactory {

    public abstract User ofNew(CreateUserDTO userDTO);

    public abstract User ofExisting(UpdateUserDTO userDTO);

    public static UserFactory of(String userType){
        UserFactory factory;
        
        if(userType.equals("customer")){
            factory = new CustomerFactory();
        }else if(userType.equals("owner")){
            factory = new OwnerFactory();
        }else{
            throw new NoSuchTypeException(userType);
        }

        return factory;
    }
}
