package project.studycafe.helper.exception.order;


import project.studycafe.helper.exception.UserException;

public class NotFindOrderItemException extends UserException {
    public NotFindOrderItemException(String message) {
        super(message);
        
    }
}

