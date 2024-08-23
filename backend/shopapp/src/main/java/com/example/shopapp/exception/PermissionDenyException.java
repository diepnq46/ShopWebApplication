package com.example.shopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PermissionDenyException extends RuntimeException{
    public PermissionDenyException(String message){
        super(message);
    }
}
