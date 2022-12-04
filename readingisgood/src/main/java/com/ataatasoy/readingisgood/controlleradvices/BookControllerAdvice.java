package com.ataatasoy.readingisgood.controlleradvices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ataatasoy.readingisgood.exceptions.BookAlreadyExistsException;
import com.ataatasoy.readingisgood.exceptions.BookNotFoundException;

@ControllerAdvice
public class BookControllerAdvice {
    @ResponseBody
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String customerNotFoundHandler(BookNotFoundException ex) {
        return ex.getMessage();
    } 

    @ResponseBody
    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String customerAlreadyExistsHandler(BookAlreadyExistsException ex) {
      return ex.getMessage();
    }
}