package com.otbs.OnlineTicketBookingSystem.controller;

import com.otbs.OnlineTicketBookingSystem.exception.BadFileException;
import com.otbs.OnlineTicketBookingSystem.exception.ChangePasswordException;
import com.otbs.OnlineTicketBookingSystem.exception.PaymentException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({ChangePasswordException.class, PaymentException.class, BadFileException.class})
    public ProblemDetail handleControllerException(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatusCode status,
                                                                  @NotNull WebRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fe : ex.getFieldErrors()) {

            stringBuilder.append(fe.getField());
            stringBuilder.append(" - ");
            stringBuilder.append(fe.getDefaultMessage());
            stringBuilder.append(". ");
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                stringBuilder.toString());
        problemDetail.setTitle("Validation Error");


        return ResponseEntity.badRequest().body(problemDetail);
    }
}
