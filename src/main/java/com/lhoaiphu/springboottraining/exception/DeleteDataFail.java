package com.lhoaiphu.springboottraining.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DeleteDataFail extends Exception{
    private static final long serialVersionUID = 1L;

    public DeleteDataFail(String code){
        super(code);
    }
}
