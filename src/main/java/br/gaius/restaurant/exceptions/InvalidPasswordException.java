package br.gaius.restaurant.exceptions;

public class InvalidPasswordException extends RuntimeException{

    private static String template = "invalid password: %s for user with id: %d.";

    public InvalidPasswordException(Long id, String password){
        super(String.format(template, password, id));
    }
}
