package com.ataatasoy.readingisgood.exceptions;

public class InvalidOrderException extends RuntimeException{
    public InvalidOrderException(String msg){
        super("Err:" + msg);
    }
    
}
