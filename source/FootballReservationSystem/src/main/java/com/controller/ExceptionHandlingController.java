package com.controller;

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
//    @ExceptionHandler(value = EntityNotFoundException.class)
//    protected ResponseEntity handleEntityNotFoundException(EntityNotFoundException ex){
//
//    }
}
