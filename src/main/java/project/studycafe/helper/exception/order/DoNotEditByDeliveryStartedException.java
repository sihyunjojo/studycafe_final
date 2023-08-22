package project.studycafe.helper.exception.order;

import project.studycafe.helper.exception.UserException;

public class DoNotEditByDeliveryStartedException extends UserException {
    public DoNotEditByDeliveryStartedException() {
        super();
    }

    public DoNotEditByDeliveryStartedException(String message) {
        super(message);
    }

    public DoNotEditByDeliveryStartedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoNotEditByDeliveryStartedException(Throwable cause) {
        super(cause);
    }
}
