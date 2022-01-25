package com.lhoaiphu.springboottraining.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundEx.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundEx ex, WebRequest request){
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadRequestEx.class)
    public ResponseEntity<?> badRequestException(BadRequestEx ex, WebRequest request){
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(GetDataFail.class)
    public ResponseEntity<?> getDataFailHandler(Exception ex, WebRequest request){
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AddDataFail.class)
    public ResponseEntity<?> addDataFailHandler(Exception ex, WebRequest request){
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateDataFail.class)
    public ResponseEntity<?> updateDataFailHandler(Exception ex, WebRequest request){
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteDataFail.class)
    public ResponseEntity<?> deleteDataFailHandler(Exception ex, WebRequest request){
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request){
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
