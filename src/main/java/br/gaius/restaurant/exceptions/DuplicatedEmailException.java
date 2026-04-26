package br.gaius.restaurant.exceptions;

public class DuplicatedEmailException extends RuntimeException {

    private static String template = "%s email already in use.";

    public DuplicatedEmailException(String email){
        super(String.format(template, email));
    }

}
