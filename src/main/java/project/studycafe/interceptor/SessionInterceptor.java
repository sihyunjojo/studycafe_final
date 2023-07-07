package project.studycafe.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.studycafe.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        log.info("session = {}", session.getAttribute(LOGIN_MEMBER));

        if (session != null && session.getAttribute(LOGIN_MEMBER) != null) {
            Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);
            request.setAttribute(LOGIN_MEMBER, loginMember); // 넣어줌으로 계속적으로 넣어줘야하는 값 자동으로 넣어줌.
        }

        return true;
    }
}
