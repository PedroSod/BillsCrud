package com.bills.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(status, ex.getBindingResult().getFieldErrors().stream()
                .map(field -> String.format("%s - %s", field.getField(), field.getDefaultMessage())).collect(Collectors.toList())), status);
    }

    @ExceptionHandler(BillInterestRulesNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleBillInterestRulesNotFoundException(BillInterestRulesNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoBIllsRecordedException.class)
    public final ResponseEntity<ErrorResponse> handleNoBIllsRecordedException(NoBIllsRecordedException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NO_CONTENT, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }
}
