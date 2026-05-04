package br.gaius.restaurant.handlers;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.gaius.restaurant.exceptions.DuplicatedEmailException;
import br.gaius.restaurant.exceptions.DuplicatedLoginException;
import br.gaius.restaurant.exceptions.InvalidPaginationParameterException;
import br.gaius.restaurant.exceptions.InvalidPasswordException;
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

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        
        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/user-not-found"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("User not found");
        problemDetail.setDetail(e.getMessage());
        
        return problemDetail;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingServletRequestParmeter(MissingServletRequestParameterException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/missing-request-parameters"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("Missing Request Parameters");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(InvalidPaginationParameterException.class)
    public ProblemDetail handleInvalidPaginationParameterException(InvalidPaginationParameterException e, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setType(URI.create("http://localhost:8080/restaurants/docs/errors/invalid-pagination-parameter"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("Invalid Pagination Parameter Request");
        problemDetail.setDetail(e.getMessage());

        return problemDetail;
    }
}
