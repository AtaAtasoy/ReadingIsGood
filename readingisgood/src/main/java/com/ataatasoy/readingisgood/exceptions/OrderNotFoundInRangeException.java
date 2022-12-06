package com.ataatasoy.readingisgood.exceptions;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public class OrderNotFoundInRangeException extends RuntimeException{
    public OrderNotFoundInRangeException(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        super("Orders not found between: " + start.toString() + " - " + end.toString());
    }
}
