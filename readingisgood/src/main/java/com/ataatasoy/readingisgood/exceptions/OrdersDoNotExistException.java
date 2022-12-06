package com.ataatasoy.readingisgood.exceptions;

public class OrdersDoNotExistException extends RuntimeException {
    public OrdersDoNotExistException(){
        super("There aren't any orders in the system");
    }
}
