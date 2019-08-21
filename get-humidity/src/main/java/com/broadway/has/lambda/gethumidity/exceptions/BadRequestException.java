package com.broadway.has.lambda.gethumidity.exceptions;

public class BadRequestException extends Exception{
    private final String original;
    private final Exception exception;

    public BadRequestException(String original, Exception e) {
        this.original = original;
        this.exception = e;
    }

    public String getOriginal() {
        return original;
    }

    public Exception getException() {
        return exception;
    }
}
