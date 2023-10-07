package project.studycafe.helper.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import project.studycafe.app.domain.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        Map<String, Object> model = modelAndView.getModel();

        if (session != null && session.getAttribute(LOGIN_MEMBER) != null) {
            Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);
            log.info("loginMember 요청값에 loginMember 로 넣어줌.");
            request.setAttribute(LOGIN_MEMBER, loginMember); // 넣어줌으로 계속적으로 넣어줘야하는 값 자동으로 넣어줌.

            if (model.get(LOGIN_MEMBER) == null) {
                log.info("세션에 로그인이 되어있고 model에 loginMember 가 없을 시, 모델에 loginMember 로 넣어줌.");
                model.put(LOGIN_MEMBER, loginMember);
            }
        }


    }
}
