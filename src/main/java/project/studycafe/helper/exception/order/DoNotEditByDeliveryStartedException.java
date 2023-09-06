package project.studycafe.helper.exception.order;

import project.studycafe.helper.exception.UserException;

public class DoNotEditByDeliveryStartedException extends UserException {
    public DoNotEditByDeliveryStartedException(String message) {
        super(message);
    }
}
