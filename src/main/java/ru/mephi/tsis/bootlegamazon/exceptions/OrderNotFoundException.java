package ru.mephi.tsis.bootlegamazon.exceptions;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(String message){
        super(message);
    }
}
