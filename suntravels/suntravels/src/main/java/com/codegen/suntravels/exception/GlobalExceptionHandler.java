package com.codegen.suntravels.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that catches and handles different types of exceptions globally for the application.
 * It processes specific exceptions like {@link ResourceNotFoundException}, validation errors, and general exceptions.
 * The handler provides a standardized response to clients for each type of exception.
 *
 * <p>This class is annotated with {@link ControllerAdvice} to handle exceptions globally across all controllers in the Spring application.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} and sends a 404 Not Found response with the exception's message.
     *
     * @param ex the {@link ResourceNotFoundException} thrown by the application
     * @return a {@link ResponseEntity} containing the error details with HTTP status 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation errors caused by invalid input parameters in the request.
     * It processes a {@link MethodArgumentNotValidException} and returns a list of validation errors.
     *
     * @param ex the {@link MethodArgumentNotValidException} containing the validation errors
     * @return a {@link ResponseEntity} containing a map of field names and their corresponding error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any other exceptions that are not specifically caught by other handlers.
     * This is a global exception handler that catches unexpected exceptions.
     *
     * @param ex the {@link Exception} that was thrown
     * @return a {@link ResponseEntity} containing the error details with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

/**
 * A simple error response class used to structure error messages and status codes.
 * It is used to send error details in the response body when an exception is thrown.
 */
class ErrorResponse {
    private int status;
    private String message;

    /**
     * Constructs an ErrorResponse with the given status and message.
     *
     * @param status the HTTP status code for the error
     * @param message the error message to be sent in the response
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
