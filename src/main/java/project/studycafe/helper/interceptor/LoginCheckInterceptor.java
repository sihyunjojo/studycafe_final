package project.studycafe.helper.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.board.Reply;
import project.studycafe.app.service.board.CommentService;
import project.studycafe.app.service.board.ReplyService;

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

    private final CommentService commentService;
    private final ReplyService replyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");

            //로그인으로 redirect
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

            log.info("redirectURL = {}", redirectURL);
            // 상대경로 절대경로 잘 구분해야한다.
            response.sendRedirect(redirectURL);
            return false;
        }
        return true;
    }
}
