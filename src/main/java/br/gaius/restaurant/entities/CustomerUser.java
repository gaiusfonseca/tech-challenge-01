package br.gaius.restaurant.entities;

public class CustomerUser extends User{

    public CustomerUser(String login, String password, String email, String name, String address) {
        super(login, password, email, name, address);
    }

    @Override
    public String getType() {
        return "Customer";
    }

}
