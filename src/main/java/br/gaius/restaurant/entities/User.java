package br.gaius.restaurant.entities;

import java.time.LocalDate;

public class User {

    private Long id;
    private String login;
    private String password;
    private String email;
    private String name;
    private String address;
    private LocalDate lastModified;
    Role role;

    private User(Builder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.password = builder.password;
        this.email = builder.email;
        this.name = builder.name;
        this.address = builder.address;
        this.lastModified = builder.lastModified;
        this.role = builder.role;
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

    public String getAddress() {
        return address;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public Role getRole(){
        return role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (login == null) {
            if (other.login != null)
                return false;
        } else if (!login.equals(other.login))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", login=" + login + ", password=" + password + ", email=" + email + ", name=" + name
                + ", address=" + address + ", lastModified=" + lastModified + ", role=" + role + "]";
    }

    public static Builder builder(){
        return new Builder();
    }

    public static Builder builder(User user){
        return new Builder(user);
    }

    public static class Builder {

        private Long id;
        private String login;
        private String password;
        private String email;
        private String name;
        private String address;
        private LocalDate lastModified;
        private Role role;

        private Builder() {

        }

        private Builder(User user) {
            this.id = user.getId();
            this.login = user.getLogin();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.address = user.getAddress();
            this.lastModified = user.getLastModified();
            this.role = user.getRole();

        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withLastModified(LocalDate lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
