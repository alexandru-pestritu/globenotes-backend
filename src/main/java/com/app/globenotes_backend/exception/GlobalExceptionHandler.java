package com.app.globenotes_backend.exception;

import com.app.globenotes_backend.dto.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(statusCode.value())
                .status(HttpStatus.valueOf(statusCode.value()))
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(statusCode.value())
                .status(HttpStatus.valueOf(statusCode.value()))
                .message("Validation error: " + fieldMessage)
                .build();

        return new ResponseEntity<>(response, statusCode);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<HttpResponse> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex) {
        log.error("SQLIntegrityConstraintViolationException: {}", ex.getMessage());
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(BAD_REQUEST.value())
                .status(BAD_REQUEST)
                .message(ex.getMessage().contains("Duplicate entry") ? "Information already exists" : ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> handleBadCredentials(BadCredentialsException ex) {
        log.error("BadCredentialsException: {}", ex.getMessage());
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(BAD_REQUEST.value())
                .status(BAD_REQUEST)
                .message("Incorrect email or password")
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> handleApiException(ApiException ex) {
        log.error("ApiException: {}", ex.getMessage());
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(BAD_REQUEST.value())
                .status(BAD_REQUEST)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("AccessDeniedException: {}", ex.getMessage());
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(FORBIDDEN.value())
                .status(FORBIDDEN)
                .message("Access denied. You don\'t have access.")
                .build();
        return new ResponseEntity<>(response, FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage());
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .status(INTERNAL_SERVER_ERROR)
                .message(ex.getMessage() != null
                        ? ex.getMessage()
                        : "Some error occurred")
                .build();
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<HttpResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error("EmptyResultDataAccessException: {}", ex.getMessage());
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(BAD_REQUEST.value())
                .status(BAD_REQUEST)
                .message(ex.getMessage().contains("expected 1, actual 0") ? "Record not found" : ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }


    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<HttpResponse> handleDataAccessException(DataAccessException ex) {
        log.error("DataAccessException: {}", ex.getMessage());
        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(BAD_REQUEST.value())
                .status(BAD_REQUEST)
                .message(processErrorMessage(ex.getMessage()))
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    private String processErrorMessage(String errorMessage) {
        if (errorMessage != null) {
            if (errorMessage.contains("Duplicate entry")) {
                return "Duplicate entry. Please try again.";
            }
        }
        return "Some error occurred";
    }
}

