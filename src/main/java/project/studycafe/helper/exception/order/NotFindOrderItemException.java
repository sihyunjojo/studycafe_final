package project.studycafe.helper.exception.order;


import project.studycafe.helper.exception.UserException;

public class NotFindOrderItemException extends UserException {
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

