package com.ecommerce.ebdify.exceptions;

import com.ecommerce.ebdify.models.dtos.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // to get more clear exception message for null values
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = "This field cannot be empty!";
            response.put(fieldName, message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // to get more clear exception message if a particular resource is not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(
            ResourceNotFoundException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(false, message);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    // To get more clear exception message when a category already exists with same name,
    // also when no category is found while fetching list of categories.
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(
            APIException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(false, message);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

}
