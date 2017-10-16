package com.controller;

import com.dto.Wrapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

/**
 * @author MinhQuy
 */
@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Wrapper> handleEntityNotFoundException(EntityNotFoundException ex){
        Wrapper wrapper = new Wrapper(null, 404, ex.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    protected ResponseEntity<Wrapper> handleDuplicateKeyException(DuplicateKeyException ex){
        Wrapper wrapper = new Wrapper(null, 400, ex.getMessage());
        return new ResponseEntity<Wrapper>(wrapper, HttpStatus.BAD_REQUEST);
    }
}
