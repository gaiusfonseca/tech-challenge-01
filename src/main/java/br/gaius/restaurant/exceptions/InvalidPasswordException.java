package br.gaius.restaurant.exceptions;

public class InvalidPasswordException extends RuntimeException{

    private static String template = "invalid password '%s' for user '%s'.";

    public InvalidPasswordException(String login, String password){
        super(String.format(template, login, password));
    }
}
