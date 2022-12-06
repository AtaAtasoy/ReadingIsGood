package com.ataatasoy.readingisgood.controlleradvices;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  ResponseEntity<Problem> customerNotFoundHandler(CustomerNotFoundException ex) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                    .withTitle("Could not find customer").
                    withDetail(ex.getMessage()));
  }

  @ResponseBody
  @ExceptionHandler(CustomerAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ResponseEntity<Problem> customerAlreadyExistsHandler(CustomerAlreadyExistsException ex) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                    .withTitle("Customer already exists").
                    withDetail(ex.getMessage()));
  }
}
