package com.ataatasoy.readingisgood.exceptions;

public class InvalidCustomerException extends RuntimeException {
    public InvalidCustomerException(String msg){
        super("Cant build model for customer with msg:" + msg);
    }
}
