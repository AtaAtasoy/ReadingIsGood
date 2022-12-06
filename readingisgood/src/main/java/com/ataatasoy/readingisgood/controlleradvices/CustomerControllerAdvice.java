package com.ataatasoy.readingisgood.controlleradvices;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ataatasoy.readingisgood.exceptions.InvalidCustomerException;

import jakarta.validation.ConstraintViolationException;

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
  @ExceptionHandler(InvalidCustomerException.class)
  ResponseEntity<Problem> invalidCustomerHandler(InvalidCustomerException ex) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                    .withTitle("Invalid customer input").
                    withDetail(ex.getMessage()));
  }


  @ResponseBody
  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<Problem> constraintViolationHandler(ConstraintViolationException ex) {
      return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
              .body(Problem.create()
                      .withTitle("Violeated constraints of order").
                      withDetail(ex.getMessage()));
  }
}
