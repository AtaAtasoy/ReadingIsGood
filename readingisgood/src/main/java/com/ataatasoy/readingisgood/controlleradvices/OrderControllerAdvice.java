package com.ataatasoy.readingisgood.controlleradvices;

import com.ataatasoy.readingisgood.exceptions.IllegalOrderMethodException;
import com.ataatasoy.readingisgood.exceptions.OrderNotFoundInRangeException;
import com.ataatasoy.readingisgood.exceptions.OrdersDoNotExistException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ataatasoy.readingisgood.exceptions.OrderNotFoundException;

@ControllerAdvice
public class OrderControllerAdvice {
    @ResponseBody
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String customerNotFoundHandler(OrderNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(OrdersDoNotExistException.class)
    ResponseEntity<Problem> ordersDoNotExistHandler(OrdersDoNotExistException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("No orders").
                        withDetail(ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(OrderNotFoundInRangeException.class)
    ResponseEntity<Problem> orderNotFoundInRangeHandler(OrderNotFoundInRangeException ex){
        return ResponseEntity
                .status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("No orders in range").
                        withDetail(ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(IllegalOrderMethodException.class)
    ResponseEntity<Problem> illegalOrderMethodHandler(IllegalOrderMethodException ex){
        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail(ex.getMessage()));
    }
}
