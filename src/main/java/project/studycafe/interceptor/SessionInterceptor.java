package project.studycafe.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.studycafe.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

// 안쓰는걸로
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        log.info("session = {}", session.getAttribute(LOGIN_MEMBER));

        if (session != null && session.getAttribute(LOGIN_MEMBER) != null) {
            Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);
            request.setAttribute(LOGIN_MEMBER, loginMember);
            // loginMember를 어디든지 꺼내 쓸 수 있는거?
            // 근데 이거 쓰려면 @Login쓰던걸 httpServletRequest 이 큰걸 가져와야하는데??
        }

        return true;
    }
}
