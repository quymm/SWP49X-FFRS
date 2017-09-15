package com.exception;

import com.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MinhQuy on 4/13/2017.
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Map<String, ErrorDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        return responseError(e.getMessage(), 400);
    }

    private Map<String, ErrorDTO> responseError(String message, Integer code) {
        Map<String, ErrorDTO> mapError = new HashMap<>();
        ErrorDTO errorDTO = new ErrorDTO(message, code);
        mapError.put("error", errorDTO);
        return mapError;
    }
}
