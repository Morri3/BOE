package com.team.boeboard.exception;

public class BoeBoardServiceException extends RuntimeException{
    public BoeBoardServiceException() {
    }

    public BoeBoardServiceException(String msg) {
        super(msg);
    }
}
