package ru.cft.freelanceservice.exceptions;

public class EmptyDTOFieldsException extends Exception {
    public EmptyDTOFieldsException() {
        super();
    }

    public EmptyDTOFieldsException(String message) {
        super(message);
    }

    public EmptyDTOFieldsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDTOFieldsException(Throwable cause) {
        super(cause);
    }

    protected EmptyDTOFieldsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
