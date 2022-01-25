package com.lhoaiphu.springboottraining.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundEx extends Exception{
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundEx(String message){
        super(message);
    }
}