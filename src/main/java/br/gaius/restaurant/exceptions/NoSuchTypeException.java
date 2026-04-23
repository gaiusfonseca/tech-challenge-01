package br.gaius.restaurant.exceptions;

public class NoSuchTypeException extends RuntimeException {

    private static String template = "only 'customer' or 'owner' type allowed. you sent '%s' type.";

    public NoSuchTypeException(String type) {
        super(String.format(template, type));
    }
}
