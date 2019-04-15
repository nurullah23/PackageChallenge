package com.mobiquityinc.packer.exceptions;

public class APIException extends Exception {

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Exception cause) {
        super(message, cause);
    }
}
