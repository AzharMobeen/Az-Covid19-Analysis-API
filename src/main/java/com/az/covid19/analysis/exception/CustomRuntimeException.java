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

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException{

    private String message;
    private String detail;

    public CustomRuntimeException(String message, String detail){
        super(message);
        this.message = message;
        this.detail = detail;
    }
}