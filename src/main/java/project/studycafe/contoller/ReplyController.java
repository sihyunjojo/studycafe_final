package project.studycafe.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.studycafe.contoller.form.ReplyForm;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.domain.Member;
import project.studycafe.domain.Reply;
import project.studycafe.service.board.BoardService;
import project.studycafe.service.board.CommentService;
import project.studycafe.service.board.ReplyService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final ReplyService replyService;

    //헤당 comment와 관련 댓글들 전부 불러오기
    @GetMapping("/{commentId}")
    public String reply(@PathVariable long commentId, Model model) {
        List<Reply> replys = replyService.findReplys();

        return "redirect:/board/" + replys.get(0).getComment().getBoard().getId();
    }

    // 댓글 생성
    @PostMapping()
    public String add(@Login Member loginMember, ReplyForm form) {
        log.info("loginMember = {}", loginMember);
        log.info("replyForm = {}", form);
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/board/" + form.getBoardId();
        }
        replyService.addReply(form);
        return "redirect:/board/" + form.getBoardId();
    }

    // 댓글 수정
    @PostMapping("/{replyId}/edit")
    public String edit(Reply reply, @PathVariable Long replyId) {
        replyService.editReply(replyId, reply);
        return "redirect:/board/" + reply.getComment().getBoard().getId();
    }

//    @PostMapping("/{boardId}/edit/likeCount")
//    public String editLikeCount(BoardForm boardForm, @PathVariable Long boardId) {
//        // 이걸하려면 멤버마다 그 보드에 대한 like를 유지하고 있는지에 대한 db 컬럼이 필요하다.
//        return "redirect:/board";
//    }

    // 댓글 삭제
    @GetMapping("/{replyId}/delete")
    public String delete(@PathVariable long replyId) {
        replyService.deleteReply(replyId);
        return "redirect:/board"; // 삭제 후 목록 페이지로 리다이렉트
    }
}
