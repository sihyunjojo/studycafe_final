package project.studycafe.helper.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.board.Reply;
import project.studycafe.app.service.board.CommentService;
import project.studycafe.app.service.board.ReplyService;
import project.studycafe.helper.interceptor.Object.ProblemResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

// 웹 애플리케이션의 요청과 응답 사이에 위치하며 요청을 가로채고 처리하는 역할을한다.
// 주로 애플리케이션의 보안,로깅,트랜잭션 관리 등과 같은 횡단 관심사를 처리하는데 사용
@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final CommentService commentService;
    private final ReplyService replyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");

            //로그인페이지로 redirect
            String redirectURL = "/login?redirectURL=" + requestURI;

            if (requestURI.contains("reply")) {
                Long replyId = Long.parseLong(requestURI.split("/")[2]);
                Optional<Reply> reply = replyService.findById(replyId);
                Long boardId = ((Comment) reply.orElseThrow().toMap().get("comment")).getBoardId();

                redirectURL = "/login?redirectURL=/board/" + boardId;
            } else if (requestURI.contains("comment")) {
                Long commentId = Long.parseLong(requestURI.split("/")[2]);
                Optional<Comment> comment = commentService.findById(commentId);
                Long boardId = comment.orElseThrow().getBoardId();

                redirectURL = "/login?redirectURL=/board/" + boardId;
            }
//            else if (requestURI.contains("delete")) {
//                response.sendRedirect("login?redirectURL=" + requestURI.replace("/delete", ""));
//            }

            // objectMapper를 통해서 html로 값 전달
            ProblemResponse problemResponse = new ProblemResponse("헤당 작업을 하려면 로그인이 필요합니다.");

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String jsonString = objectMapper.writeValueAsString(problemResponse);
            response.getWriter().write(jsonString);


            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // 상대경로 절대경로 잘 구분해야한다.
            response.sendRedirect(redirectURL);

//          바로 위에 줄까지의 정보는 기억을 하고 다음에 실행되는 것을 전부 생략한다.
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //뷰에 데이터 추가: postHandle() 메서드를 사용하여 컨트롤러의 작업 결과를 뷰에 추가적인 데이터로 전달할 수 있습니다. 이를 통해 뷰를 렌더링할 때 데이터를 활용할 수 있습니다.
        //뷰 선택 변경: postHandle() 메서드를 사용하여 컨트롤러의 작업 결과에 따라 렌더링할 뷰를 동적으로 선택하거나 변경할 수 있습니다.
        //후처리 작업: 컨트롤러의 작업 결과에 대한 후처리 작업을 수행할 수 있습니다. 예를 들어, 로깅 작업이나 세션 관리와 같은 작업을 수행할 수 있습니다.
        //성능 측정: 컨트롤러의 실행 시간 등을 측정하여 성능 모니터링을 위한 정보를 수집할 수 있습니다.
    }
}
