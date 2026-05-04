package br.gaius.restaurant.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static String idTemplate = "No user found with id: '%d'.";
    private static String LoginTemplate = "No user found with login: '%s'.";

    public UserNotFoundException(Long id){
        super(String.format(idTemplate, id));
    }

    public UserNotFoundException(String login){
        super(String.format(LoginTemplate, login));
    }

}
