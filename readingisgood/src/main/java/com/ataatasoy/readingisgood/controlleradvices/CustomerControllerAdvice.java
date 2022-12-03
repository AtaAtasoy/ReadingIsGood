package com.ataatasoy.readingisgood.controlleradvices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ataatasoy.readingisgood.exceptions.CustomerAlreadyExistsException;
import com.ataatasoy.readingisgood.exceptions.CustomerNotFoundException;

@ControllerAdvice
public class CustomerControllerAdvice {
  @ResponseBody
  @ExceptionHandler(CustomerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String customerNotFoundHandler(CustomerNotFoundException ex) {
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(CustomerAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String customerAlreadyExistsHandler(CustomerAlreadyExistsException ex) {
    return ex.getMessage();
  }
}
