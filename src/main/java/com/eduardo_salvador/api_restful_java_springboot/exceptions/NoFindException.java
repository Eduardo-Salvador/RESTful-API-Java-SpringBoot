package com.eduardo_salvador.api_restful_java_springboot.exceptions;
import java.util.NoSuchElementException;

public class NoFindException extends NoSuchElementException {
    public NoFindException(){
        super("Products is empty");
    }

    public NoFindException(String message){
        super(message);
    }
}