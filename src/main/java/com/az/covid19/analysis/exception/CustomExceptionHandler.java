package com.az.covid19.analysis.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @Value("${error.message.unauthorized:Provided username/password is invalid}")
    private String unauthorizedMessage;

    @Value("${error.message.expiredJwt:Provided jwt is expired}")
    private String expiredJwtMessage;

    @Value("${error.message.currentJwt:Provided jwt is not correct}")
    private String currentJwtMessage;

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity handleBadCredentialsException(BadCredentialsException exception, WebRequest request){
        ErrorMessage errorMessage = buildErrorMessage(exception.getMessage(), request, unauthorizedMessage);
        return new ResponseEntity(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity handleExpiredJwtException(ExpiredJwtException exception, WebRequest request){
        ErrorMessage errorMessage = buildErrorMessage(expiredJwtMessage, request, exception.getMessage());
        return new ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    public ResponseEntity handleMalformedJwtException(MalformedJwtException exception, WebRequest request){
        ErrorMessage errorMessage = buildErrorMessage(currentJwtMessage, request, exception.getMessage());
        return new ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {CustomRuntimeException.class})
    public ResponseEntity handleCustomRuntimeException(CustomRuntimeException exception, WebRequest request){
        ErrorMessage errorMessage = buildErrorMessage(exception.getMessage(), request, exception.getDetail());
        return new ResponseEntity(errorMessage, exception.getHttpStatus());
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity handleRuntimeException(RuntimeException exception, WebRequest request){
        ErrorMessage errorMessage = buildErrorMessage(exception.getMessage(), request,
                HttpStatus.NOT_IMPLEMENTED.name());
        return new ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorMessage buildErrorMessage(String exception, WebRequest request, String detail){
        String path = request.getDescription(false).split("=")[1];
        return ErrorMessage.of().localDateTime(LocalDateTime.now())
                .message(exception)
                .path(path)
                .detail(detail)
                .build();
    }
}
