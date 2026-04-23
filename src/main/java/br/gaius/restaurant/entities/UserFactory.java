package br.gaius.restaurant.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.gaius.restaurant.dtos.CreateUserDTO;
import br.gaius.restaurant.dtos.UpdateUserDTO;
import br.gaius.restaurant.exceptions.NoSuchTypeException;

public abstract class UserFactory {

    
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
    
    public abstract User fromCreateRequest(CreateUserDTO createDTO);

    public abstract User fromUpdateRequest(UpdateUserDTO updateDTO);

    public abstract User fromChangePasswordRequest(Long id, String newPassword);

    public abstract User fromReadDB(ResultSet rs) throws SQLException;

    public abstract User fromSaveDB(Long generatedKey, User user);

    public abstract User fromUpdateDB(User user);
}
