package ru.mephi.tsis.bootlegamazon.exceptions;

public class ArticleNotFoundException extends Exception{
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
