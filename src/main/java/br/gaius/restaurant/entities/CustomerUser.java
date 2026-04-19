package br.gaius.restaurant.entities;

import java.time.LocalDate;

public class CustomerUser extends User{

    //construtor visível apenas a nível de pacote
    CustomerUser(Long id, String login, String password, String email, String name, String address, LocalDate lastModified) {
        super(id, login, password, email, name, address, lastModified);
    }

    @Override
    public String getType() {
        return "customer";
    }

}
