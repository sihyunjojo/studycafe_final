package project.studycafe.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/popup")
@RequiredArgsConstructor
public class PopupController {

//    @GetMapping()
//    public String showPopup(HttpServletRequest request) {
//        log.info("popup Start");
//        String noticeCookie = getCookie(request, "name");
//        if (noticeCookie == null || !noticeCookie.equals("value")) {
//            return "popup/hello";
//        }
//        return "redirect:/"; // 쿠키 값이 있는 경우 메인 페이지로 리다이렉트
//    }
//
//    public String getCookie(HttpServletRequest request, String name) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(name)) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }

    @GetMapping()
    public String home(Model model, HttpServletRequest request) {
        // 쿠키 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("popupShown") && cookie.getValue().equals("true")) {
                    // 이미 팝업을 보았으면 홈 화면으로 이동
                    log.info("cookie={},{}",cookie.getValue(), cookie.getName());
                    return "/popup/closePopup";

                }
            }
        }
        return "/popup/hello";
    }

    @PostMapping("/closePopup")
    public String closePopup(HttpServletResponse response, @RequestParam(name = "option", defaultValue = "false") boolean option) {
        // 쿠키 설정
        log.info("option={}", option);
        if (option == true) {
            Cookie popupShownCookie = new Cookie("popupShown", "true");
            popupShownCookie.setMaxAge(60 * 60 * 24); // 1일 동안 유지
            response.addCookie(popupShownCookie);
        }

        return "/popup/closePopup";
    }
}
