package project.studycafe.helper.exceptionHandler.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.studycafe.helper.exception.BadRequestException;
import project.studycafe.helper.exception.UserException;
import project.studycafe.helper.exceptionHandler.ErrorResult;

import javax.servlet.http.HttpServletRequest;

// 핸들러는 요청을 처리하는 객체 또는 메서드를 가리키며, 해당 요청을 처리하는 로직을 포함한다.
//@ControllerAdvice는 스프링 프레임워크에서 제공하는 어노테이션으로, 전역적인 예외 처리와 바인딩 설정을 위한 클래스를 정의할 때 사용됩니다
//전역적인 예외 처리:@ControllerAdvice 클래스 내부에@ExceptionHandler 어노테이션을 사용하여 예외를 처리하는 메서드를 정의할 수 있습니다.이렇게 정의한 메서드는 여러 컨트롤러에서 발생하는 예외를 효과적으로 처리할 수 있습니다.
//전역적인 모델 설정:@ModelAttribute 어노테이션을 사용하여 모델 속성을 전역적으로 설정할 수 있습니다.이렇게 하면 각각의 컨트롤러 메서드에서 동일한 모델 속성을 설정하지 않아도 됩니다.
//전역적인 데이터 바인딩 설정:@InitBinder 어노테이션을 사용하여 데이터 바인딩 설정을 전역적으로 정의할 수 있습니다.이렇게 하면 여러 컨트롤러에서 동일한 데이터 바인딩 설정을 공유할 수 있습니다.
//전역적인 뷰 설정:@ModelAttribute 어노테이션을 사용하여 모든 뷰에서 사용되는 공통 데이터를 설정할 수 있습니다.

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 클라이언트측 에러를 발생시켜줘라
    @ExceptionHandler(RuntimeException.class) // 런타임에러가 발생시 발생한다. 2개이상 가능
    public ModelAndView badRequestHandler(BadRequestException e) {
        log.error("[badRequestHandler] ex", e);
        return new ModelAndView("error/500");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 클라이언트측 에러를 발생시켜줘라
    @ExceptionHandler({UserException.class}) // 런타임에러가 발생시 발생한다. 2개이상 가능
    public ModelAndView userExceptionHandler(UserException e, HttpServletRequest request, Model model) {
        log.error("[badRequestHandler] ex", e);
        model.addAttribute("errorMessage", e.getMessage());

        String postUrl = (String) request.getAttribute("postUrl");
        log.info("postUrl = {}", postUrl);

        String[] urlParts = postUrl.split("/");
        log.info("urlparts = {},{},{},{}", urlParts[0], urlParts[1], urlParts[2], urlParts[3]);

        String previousUrl;
        // previousUrl 결정
        switch (urlParts[1]) {
            case "board":
            case "product":
            case "member":
                previousUrl = postUrl.replace(urlParts[-1], "");
                break;
            case "order":
                if (postUrl.contains("cart")) {
                    previousUrl = "/cart";
                } else if (postUrl.contains("now")) {
                    previousUrl = "/product/" + urlParts[2];
                } else {
                    previousUrl = "/order/" + urlParts[2];
                }
                break;
            default:
                previousUrl = "";
        }


        model.addAttribute("previousUrl", previousUrl);
        return new ModelAndView("error/400");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류입니다. 어떤 오류인지 알려주세요.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class) // 2개이상 가능
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        // code,message
        return new ErrorResult("BAD", e.getMessage());
    }

    // json방식 api로 통신시.
//    @ExceptionHandler() //생략시 메서드 파라메터의 값이 지정된다.
//    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
//        log.error("[exceptionHandler] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        //	       ResponseEntity(@Nullable T body, HttpStatus status)
//        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
//    }

}
