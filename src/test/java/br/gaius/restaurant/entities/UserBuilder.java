package br.gaius.restaurant.entities;

import java.time.LocalDate;

import br.gaius.restaurant.exceptions.NoSuchTypeException;

public class UserBuilder {

    private Long id;
    private String login;
    private String password;
    private String email;
    private String name;
    private String address;
    private LocalDate lastModified;
    private String userType;

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public UserBuilder withLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public UserBuilder withUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public User build(){
        if(userType.equals("customer")){
            return new CustomerUser(id, login, password, email, name, address, lastModified);
        }else if(userType.equals("owner")){
            return new OwnerUser(id, login, password, email, name, address, lastModified);
        }else{
            throw new NoSuchTypeException(userType);
        }
    }

}
