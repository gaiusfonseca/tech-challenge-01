package br.gaius.restaurant.entities;

public class OwnerUser extends User {

    public OwnerUser(String login, String password, String email, String name, String address) {
        super(login, password, email, name, address);
    }

    @Override
    public String getType() {
        return "Owner";
    }
}
