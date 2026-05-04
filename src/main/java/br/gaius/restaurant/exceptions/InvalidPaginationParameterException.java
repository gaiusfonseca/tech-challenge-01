package br.gaius.restaurant.exceptions;

public class InvalidPaginationParameterException extends RuntimeException {

    private static String template = "page and size must be 1 or higer. page = %d, size = %d.";

    public InvalidPaginationParameterException(int page, int size) {
        super(String.format(template, page, size));
    }
}
