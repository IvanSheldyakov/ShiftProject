package ru.cft.freelanceservice.exceptions;

public class CustomerIsAlreadyRegisteredException extends Exception {

    public CustomerIsAlreadyRegisteredException() {
        super();
    }

    public CustomerIsAlreadyRegisteredException(String message) {
        super(message);
    }

    public CustomerIsAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerIsAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

}
