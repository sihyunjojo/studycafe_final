package project.studycafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// exception handler 을 만들어줌 ?
@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "error.bad") //messages.properties
public class BadRequestException extends RuntimeException { //runtime 은 원래 500
}
