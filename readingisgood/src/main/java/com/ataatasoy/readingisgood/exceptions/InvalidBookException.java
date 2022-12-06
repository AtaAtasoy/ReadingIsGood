package com.ataatasoy.readingisgood.exceptions;


public class InvalidBookException extends RuntimeException {
    public InvalidBookException(String msg){
        super("Invalid Book: with msg\"" + msg + "\"");
    }
}