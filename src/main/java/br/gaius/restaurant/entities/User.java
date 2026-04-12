package br.gaius.restaurant.entities;

import java.time.LocalDate;

import br.gaius.restaurant.dtos.UserDTO;
import br.gaius.restaurant.exceptions.InvalidPasswordException;

public class User {

    private long id;
    private String email;
    private String name;
    private String login;
    private String password;
    private String address;
    private LocalDate lastModified;

    
    public User(String email, String name, String login, String password, String address) {
        this.email = email;
        this.name = name;
        this.login = login;
        this.password = password;
        this.address = address;
        lastModified = LocalDate.now();
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        setLastModified();
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setLastModified();
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        setLastModified();
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String oldPassword, String newPassword) {
        if(!getPassword().equals(oldPassword)){
            throw new InvalidPasswordException(oldPassword);
        }

        password = newPassword;
        setLastModified();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        setLastModified();
        this.address = address;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    private void setLastModified() {
        lastModified = LocalDate.now();
    }

    public static User fromDTO(UserDTO userDTO) {
        return new User(userDTO.email(), userDTO.name(), userDTO.login(), userDTO.password(), userDTO.address());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", name=" + name + ", login=" + login + ", password=" + password
                + ", lastModified=" + lastModified + "]";
    }
}
