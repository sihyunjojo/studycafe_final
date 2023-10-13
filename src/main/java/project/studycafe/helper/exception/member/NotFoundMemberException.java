package project.studycafe.helper.exception.member;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException() {
        super("멤버를 찾을 수 없습니다.");
    }

    public NotFoundMemberException(String message) {
        super(message);
    }

    public NotFoundMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundMemberException(Throwable cause) {
        super(cause);
    }

    public NotFoundMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
