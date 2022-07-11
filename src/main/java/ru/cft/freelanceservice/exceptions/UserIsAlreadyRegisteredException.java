package ru.cft.freelanceservice.exceptions;

public class UserIsAlreadyRegisteredException extends Exception {

    public UserIsAlreadyRegisteredException() {
        super();
    }

    public UserIsAlreadyRegisteredException(String message) {
        super(message);
    }

    public UserIsAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

}
