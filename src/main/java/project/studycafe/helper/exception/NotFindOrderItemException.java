package project.studycafe.helper.exception;


public class NotFindOrderItemException extends RuntimeException {
    public NotFindOrderItemException() {
        super();
    }

    public NotFindOrderItemException(String message) {
        super(message);
    }

    public NotFindOrderItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFindOrderItemException(Throwable cause) {
        super(cause);
    }
}

