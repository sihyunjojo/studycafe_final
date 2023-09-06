package project.studycafe.helper.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import project.studycafe.app.controller.form.board.BoardForm;
import project.studycafe.app.domain.Order;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.enums.MemberLevel;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.Product;
import project.studycafe.app.service.product.ProductService;
import project.studycafe.helper.exception.LackOfMemberLevelException;
import project.studycafe.helper.exception.UserException;
import project.studycafe.helper.exceptionHandler.ProblemResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static project.studycafe.SessionConst.LOGIN_MEMBER;
import static project.studycafe.app.domain.enums.MemberLevel.MASTER;
import static project.studycafe.app.domain.enums.MemberLevel.USER;

@Slf4j
@RequiredArgsConstructor

public class AccessControlByLevelInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);

        MemberLevel memberLevel;
        if (loginMember != null) {
            memberLevel = loginMember.getMemberLevel();
        } else {
            throw new UserException("로그인한 멤버가 존재하지 않습니다. 로그인을 해주세요");
        }

        String requestURI = request.getRequestURI();
        String[] splitURI = requestURI.split("/");

        String redirectURI;

        switch (splitURI[1]) {
            case "product":
//                Long productId = Long.valueOf(splitURI[2]);
//                Optional<Product> product = productService.findById(productId);
                redirectURI = "/" + splitURI[1];
                break;
            default:
                throw new RuntimeException("로직상 문제가 있는거 같습니다.");
        }


        if (memberLevel.equals(MASTER)) {
            log.info("MASTER 권한인 사람입니다");
            return true;
        } else {
            log.info("권한이 부족합니다.");
            // 이 코드 쓰면 추가로 아무런 동적인 작업을 할 수 없어서 브라우저 기본 작동 동작 발생.
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

//            response.setContentType("application/json");
//            String jsonResponse = "{\"message\": \"권한이 부족합니다.\"}";
//            ProblemResponse problemResponse = new ProblemResponse("헤당 작업을 하기위한 회원의 권한이 부족합니다.");
//            String jsonResponse = objectMapper.writeValueAsString(problemResponse);
//            response.getWriter().write(jsonResponse);

//            response.sendRedirect(redirectURI);

            // 아무 명령도 안주면, 그냥 흰색 화면 뜸.
            return false;
        }

    }
}


