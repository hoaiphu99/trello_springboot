package com.lhoaiphu.springboottraining.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class AddDataFail extends Exception{
    private static final long serialVersionUID = 1L;

    public AddDataFail(String code){
        super(code);
    }
}