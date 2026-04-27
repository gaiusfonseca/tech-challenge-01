package br.gaius.restaurant.handlers;

import java.net.URI;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.gaius.restaurant.exceptions.DuplicatedEmailException;
import br.gaius.restaurant.exceptions.DuplicatedLoginException;
import br.gaius.restaurant.exceptions.InvalidPasswordException;
import br.gaius.restaurant.exceptions.NoSuchTypeException;
import br.gaius.restaurant.exceptions.UnsupportedFieldException;
import br.gaius.restaurant.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ProblemDetail handleDuplicatedEmail(DuplicatedEmailException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT.value());
        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/duplicated-email"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("Duplicated Email");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(DuplicatedLoginException.class)
    public ProblemDetail handleDuplicatedLogin(DuplicatedLoginException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT.value());
        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/duplicated-login"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("Duplicated Login");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ProblemDetail handleInvalidPassword(InvalidPasswordException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/invalid-password"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("Invalid Password");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(NoSuchTypeException.class)
    public ProblemDetail handleNoSuchType(NoSuchTypeException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/no-such-type"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("No Such Type");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(UnsupportedFieldException.class)
    public ProblemDetail handleUsupportedField(UnsupportedFieldException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/unsupported-field"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("Unsupported Field");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    public ProblemDetail handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/user-not-found"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("User not found");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }
}
