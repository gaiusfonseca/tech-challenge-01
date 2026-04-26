package br.gaius.restaurant.exceptions;

public class DuplicatedLoginException extends RuntimeException {

    private static String template = "%s login already in use.";

    public DuplicatedLoginException(String login){
        super(String.format(template, login));
    }
}
