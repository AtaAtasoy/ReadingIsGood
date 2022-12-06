package com.ataatasoy.readingisgood.exceptions;

public class InvalidCustomerException extends RuntimeException {
    public InvalidCustomerException(String msg){
        super("Err with msg:" + msg);
    }
}
