package project.studycafe.helper.handler.exceptionHandler.advice;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.studycafe.helper.exception.BadRequestException;
import project.studycafe.helper.exception.order.DoNotEditByDeliveryStartedException;
import project.studycafe.helper.exception.order.NotFindOrderItemException;
import project.studycafe.helper.exception.UserException;
import project.studycafe.helper.handler.exceptionHandler.ErrorResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

// 핸들러는 요청을 처리하는 객체 또는 메서드를 가리키며, 해당 요청을 처리하는 로직을 포함한다.
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
        log.info("urlparts = {},{},{},{}", urlParts[0],urlParts[1],urlParts[2],urlParts[3]);

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
