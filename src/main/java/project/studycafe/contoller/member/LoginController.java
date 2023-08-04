package project.studycafe.contoller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.studycafe.domain.form.LoginForm;
import project.studycafe.domain.member.Member;
import project.studycafe.service.login.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm form){
        return "/login/loginForm";
    }

    //@ModelAttribute 가 form에서 loginForm객체 내부의 필드와 이름이 같은 값을 가져와서 객체에 넣어줌
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                        @RequestParam(required = false, defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        log.info("login start");
        //redirectUrl은 인터셉트를 통해서 얻어지는거 였음.

        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        Member loginMember = loginService.login(form.getUserLoginId(), form.getUserPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "/login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        log.info("Logout request = {}", request);

        HttpSession session = request.getSession(false);
        log.info("session = {}", session);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
