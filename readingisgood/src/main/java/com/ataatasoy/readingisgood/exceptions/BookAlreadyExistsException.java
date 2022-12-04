package com.ataatasoy.readingisgood.exceptions;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String name){
        super("Book: \"" + name + "\" already exists.");
    }
}