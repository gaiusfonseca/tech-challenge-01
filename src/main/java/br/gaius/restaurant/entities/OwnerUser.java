package br.gaius.restaurant.entities;

import java.time.LocalDate;

public class OwnerUser extends User {

    //construtor visível apenas a nível de pacote
    OwnerUser(Long id, String login, String password, String email, String name, String address, LocalDate lastModified) {
        super(id, login, password, email, name, address, lastModified);
    }

    @Override
    public String getType() {
        return "owner";
    }
}
