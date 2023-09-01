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
@RequiredArgsConstructor
public class ExceptionMessageAdvice {

    private final ObjectMapper objectMapper;

//    json 방식: api 로 통신시 사용?
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @GetMapping("/handler/please/login")
    public ResponseEntity<ProblemResponse> pleaseLoginHandler() {
        ProblemResponse problemResponse = new ProblemResponse("헤당 작업을 하려면 로그인이 필요합니다.");

//        바티,헤더,상태 넣을 수 있음
//        	       ResponseEntity(@Nullable T body, HttpStatus status)
        return new ResponseEntity(problemResponse, HttpStatus.UNAUTHORIZED);
    }

}
