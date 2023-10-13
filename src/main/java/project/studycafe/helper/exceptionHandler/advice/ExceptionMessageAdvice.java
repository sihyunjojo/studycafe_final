package project.studycafe.helper.exceptionHandler.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import project.studycafe.helper.exceptionHandler.ErrorResult;
import project.studycafe.helper.exceptionHandler.ProblemResponse;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
@RequestMapping("/handler")
@RequiredArgsConstructor
public class ExceptionMessageAdvice {

    private final ObjectMapper objectMapper;

//    json 방식: api 로 통신시 사용?
    @GetMapping("/please/login")
    public ResponseEntity<ProblemResponse> pleaseLoginHandler() {
        ProblemResponse problemResponse = new ProblemResponse("헤당 작업을 하려면 로그인이 필요합니다.");
//        바디,헤더,상태 넣을 수 있음
//        	       ResponseEntity(@Nullable T body, HttpStatus status)
        return new ResponseEntity(problemResponse, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/not/owner")
    public ResponseEntity<ProblemResponse> notOwnerHandler() {
        ProblemResponse problemResponse = new ProblemResponse("헤당 객체의 소유자가 아닙니다.");

//        바디,헤더,상태 넣을 수 있음
//        	       ResponseEntity(@Nullable T body, HttpStatus status)
        return new ResponseEntity(problemResponse, HttpStatus.FORBIDDEN);
    }

//    클라이언트 측에서는 JavaScript를 사용하여 AJAX 요청을 보내고, 403 상태 코드를 확인한 후 원하는 동작을 수행할 수 있습니다. 이런식으로 브라우저의 기본 동작을 무시하고 클라이언트 측에서 사용자 경험을 커스터마이징할 수 있습니다.
//    예를 들어, 클라이언트 측에서는 403 상태 코드를 확인하고, 해당 코드를 감지한 경우 팝업창을 띄우거나 사용자에게 메시지를 표시하는 등의 동작을 수행할 수 있습니다.
    @GetMapping("/lack/level")
    public ResponseEntity<ProblemResponse> lackOfMemberLevelHandler() {
        ProblemResponse problemResponse = new ProblemResponse("헤당 작업을 하기위한 회원의 권한이 부족합니다.");

//        바디,헤더,상태 넣을 수 있음
//        	       ResponseEntity(@Nullable T body, HttpStatus status)
        return new ResponseEntity(problemResponse, HttpStatus.FORBIDDEN);
    }

}
