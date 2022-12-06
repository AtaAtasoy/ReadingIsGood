package com.ataatasoy.readingisgood.exceptions;

import com.ataatasoy.readingisgood.models.Book;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class InvalidBookException extends RuntimeException {
    public InvalidBookException(String msg){
        super("Invalid Book: with msg\"" + msg + "\"");
    }
}