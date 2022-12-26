package ru.mephi.tsis.bootlegamazon.exceptions;

public class CartNotFoundException extends Exception{
    public CartNotFoundException(String message) {
        super(message);
    }
}
