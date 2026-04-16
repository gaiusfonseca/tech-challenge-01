package br.gaius.restaurant.entities;

import java.time.LocalDate;

public abstract class User {

    private Long id;
    private String login;
    private String password;
    private String email;
    private String name;
    private String address;
    private LocalDate lastModified;

    
    public User(String login, String password, String email, String name, String address) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // TODO implementar regras para validar senha
        // comprimento, pelo menos uma maiuscula
        // pelos um caractere especial
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public abstract String getType();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("User[id = %d, login = %s, password = %s, email = %s, name = %s, address = %s, lastModified = %s]",
            getId(), getLogin(), getPassword(), getEmail(), getName(), getAddress(), getLastModified()
        );
    }
}
