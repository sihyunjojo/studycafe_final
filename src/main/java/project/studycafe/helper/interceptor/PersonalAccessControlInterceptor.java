package project.studycafe.helper.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.studycafe.app.domain.Cart;
import project.studycafe.app.domain.Order;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.board.Reply;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.OrderService;
import project.studycafe.app.service.board.BoardService;
import project.studycafe.app.service.board.CommentService;
import project.studycafe.app.service.board.ReplyService;
import project.studycafe.app.service.cart.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@RequiredArgsConstructor
public class PersonalAccessControlInterceptor implements HandlerInterceptor {

    private final BoardService boardService;
    private final OrderService orderService;
    private final CartService cartService;
    private final CommentService commentService;
    private final ReplyService replyService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);

        Long loginMemberId;
        if (loginMember != null) {
            loginMemberId = loginMember.getId();
        } else {
            loginMemberId = null;
        }

        String requestURI = request.getRequestURI();
        String[] splitURI = requestURI.split("/");

        // validationObject
        Long memberIdByObject = null;
        String redirectURI = Arrays.stream(splitURI).collect(Collectors.toList()).subList(0,3).toString();

        switch (splitURI[1]) {
            case "board":
                Long boardId = Long.valueOf(splitURI[2]);
                Optional<Board> board = boardService.findById(boardId);
                memberIdByObject = board.orElseThrow().getMemberId();
                break;
            case "order":
                Long orderId = Long.valueOf(splitURI[2]);
                Optional<Order> order = orderService.findById(orderId);
                memberIdByObject = order.orElseThrow().getMember().getId();
                break;
            case "cart":
                Long cartId = Long.valueOf(splitURI[2]);
                Optional<Cart> cart = cartService.findById(cartId);
                memberIdByObject = cart.orElseThrow().getMember().getId();
                redirectURI = "/product";
                break;
            case "comment":
                Long commentId = Long.valueOf(splitURI[2]);
                Optional<Comment> comment = commentService.findById(commentId);
                memberIdByObject = comment.orElseThrow().getMemberId();
                boardId = comment.orElseThrow().getBoardId();

                redirectURI = "/board/" + boardId;
                break;
            case "reply":
                Long replyId = Long.valueOf(splitURI[2]);
                Optional<Reply> reply = replyService.findById(replyId);
                memberIdByObject = reply.orElseThrow().getMemberId();
                boardId = commentService.findCommentByRepliesContaining(reply.orElseThrow()).orElseThrow().getBoardId();

                redirectURI = "/board/" + splitURI[2];
                break;
            default:
                throw new RuntimeException("로직상 문제가 있는거 같습니다.");
        }

        // 작성자가 자신인지 확인
        if (loginMember != null && loginMemberId.equals(memberIdByObject)) {
            // 작성자가 로그인한 사람입니다.
            log.info("작성자가 로그인한 사람입니다");
            return true;
        } else {
            log.info("작성자와 로그인한 사람이 다른 사람 입니다.");
            // 자신이 아닌 경우 처리 (예: 권한 부족 페이지로 리다이렉션)
            response.sendRedirect(redirectURI);
            return false;
        }
    }
}