package project.studycafe.helper.exception.order;

import project.studycafe.helper.exception.UserException;

public class NotEnoughStockException extends UserException {
    public NotEnoughStockException(String message) {
        super(message);
    }
}
