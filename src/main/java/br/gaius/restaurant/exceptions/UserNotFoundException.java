package br.gaius.restaurant.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static String template = "No user found with login: '%s'.";

    public UserNotFoundException(String login){
        super(String.format(template, login));
    }

}
