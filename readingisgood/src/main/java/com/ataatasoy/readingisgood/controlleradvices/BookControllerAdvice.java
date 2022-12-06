package com.ataatasoy.readingisgood.controlleradvices;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ataatasoy.readingisgood.exceptions.InvalidBookException;

import jakarta.validation.ConstraintViolationException;

import com.ataatasoy.readingisgood.exceptions.BookNotFoundException;

@ControllerAdvice
public class BookControllerAdvice {
    @ResponseBody
    @ExceptionHandler(BookNotFoundException.class)
    ResponseEntity<Problem> bookNotFoundHandler(BookNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Could not find book").
                        withDetail(ex.getMessage()));
    } 

    @ResponseBody
    @ExceptionHandler(InvalidBookException.class)
    ResponseEntity<Problem> invalidBookHandler(InvalidBookException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Invalid book input").
                        withDetail(ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Problem> constraintViolationHandler(ConstraintViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Violeated constraints of book").
                        withDetail(ex.getMessage()));
    }
}