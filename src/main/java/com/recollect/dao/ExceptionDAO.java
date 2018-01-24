package com.recollect.dao;

public class ExceptionDAO extends RuntimeException {

    public ExceptionDAO(String message, Throwable cause) {
        super(message, cause);
    }
}
