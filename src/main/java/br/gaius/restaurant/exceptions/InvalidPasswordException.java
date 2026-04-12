package br.gaius.restaurant.exceptions;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException(String password){
        super(buildMessage(password));
    }

    private static String buildMessage(String password) {
        return String.format("Senha inválida: %s. Não confere com a senha atual.", password);
    }
}
