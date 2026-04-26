package br.gaius.restaurant.exceptions;

public class UnsupportedFieldException extends RuntimeException {

    private static String template = "field %s not supported.";

    public UnsupportedFieldException(String textField){
        super(String.format(template, textField));
    }
}
