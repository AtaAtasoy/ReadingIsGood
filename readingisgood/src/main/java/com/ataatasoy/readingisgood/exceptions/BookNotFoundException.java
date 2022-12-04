package com.ataatasoy.readingisgood.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id){
        super("Could not find book with id: " + id);
    }
}
