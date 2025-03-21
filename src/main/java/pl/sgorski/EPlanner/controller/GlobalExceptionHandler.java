package pl.sgorski.EPlanner.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ProblemDetail handleException(Exception ex) {
        ProblemDetail errorDetail = null;
        ex.printStackTrace();

        if (ex instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(401), ex.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");

            return errorDetail;
        }

        if (ex instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "The account is locked");
        }

        if (ex instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

        if (ex instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (ex instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), ex.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if(ex instanceof NullPointerException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(500), ex.getMessage());
            errorDetail.setProperty("description", "Cannot pass empty field");
            return errorDetail;
        }

        if(ex instanceof ConstraintViolationException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(406), ex.getMessage());
            errorDetail.setProperty("description", "Validation failed");
            return errorDetail;
        }

        if(ex instanceof SQLException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(406), ex.getMessage());
            errorDetail.setProperty("description", "Violation in database");
            return errorDetail;
        }

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(500), ex.getMessage());
        errorDetail.setProperty("description", "Unknown internal server error.");
        return errorDetail;
    }
}
