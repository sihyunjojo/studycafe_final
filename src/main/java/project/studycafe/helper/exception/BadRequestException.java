package project.studycafe.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// exception handler 을 만들어줌
// bad_Request 즉, 400 에러가 발생시에 BadRequestException 로 바꿔준다.
//reason 속성은 예외 발생 시 반환할 메시지를 정의하는 데 사용됩니다.
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") //messages.properties
public class BadRequestException extends RuntimeException { //runtime 은 원래 500
}
