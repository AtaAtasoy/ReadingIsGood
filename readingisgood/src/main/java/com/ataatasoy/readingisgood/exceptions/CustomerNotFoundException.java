package com.ataatasoy.readingisgood.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(long id){
        super("Could not find customer " + id);
    }
}
