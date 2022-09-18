package ru.cft.freelanceservice.exceptions;

public class ExecutorIsAlreadyRegisteredException extends Exception {
    public ExecutorIsAlreadyRegisteredException() {
        super();
    }

    public ExecutorIsAlreadyRegisteredException(String message) {
        super(message);
    }

    public ExecutorIsAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutorIsAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }
}
