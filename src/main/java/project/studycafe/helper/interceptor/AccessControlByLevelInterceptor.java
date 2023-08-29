package project.studycafe.helper.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import project.studycafe.app.controller.form.board.BoardForm;
import project.studycafe.app.domain.Order;
import project.studycafe.app.domain.enums.MemberLevel;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static project.studycafe.SessionConst.LOGIN_MEMBER;
import static project.studycafe.app.domain.enums.MemberLevel.MASTER;
import static project.studycafe.app.domain.enums.MemberLevel.USER;

@Slf4j
public class AccessControlByLevelInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);

        MemberLevel memberLevel;
        if (loginMember != null) {
            memberLevel = loginMember.getMemberLevel();
        } else {
            memberLevel = null;
        }

        // 작성자가 자신인지 확인
        if (loginMember != null && memberLevel.equals(MASTER)) {
            log.info("MASTER 권한인 사람입니다");
        } else {
            log.info("권한이 부족합니다.");
            // 자신이 아닌 경우 처리 (예: 권한 부족 페이지로 리다이렉션)
            response.sendRedirect("/access-denied");
        }
    }
}

