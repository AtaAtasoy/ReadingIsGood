package com.ataatasoy.readingisgood.exceptions;

import com.ataatasoy.readingisgood.models.OrderStatus;

public class IllegalOrderMethodException extends RuntimeException{

    public IllegalOrderMethodException(OrderStatus status){
        super("You can't cancel an order that is in the " + status + " status");
    }
}
