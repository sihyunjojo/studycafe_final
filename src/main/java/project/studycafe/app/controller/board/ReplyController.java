package project.studycafe.app.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.studycafe.app.domain.board.Reply;
import project.studycafe.app.controller.form.board.ReplyForm;
import project.studycafe.app.domain.member.Member;
import project.studycafe.helper.resolver.argumentresolver.Login;

import project.studycafe.app.service.board.BoardService;
import project.studycafe.app.service.board.CommentService;
import project.studycafe.app.service.board.ReplyService;

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
        List<Reply> replies = replyService.findReplys();
        //repliesform을 만들어줘야할거같
        model.addAttribute("replies", replies);
        return "redirect:/board/" + commentId;
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
    public String edit(ReplyForm reply, @PathVariable Long replyId) {
        replyService.editReply(replyId, reply);
        return "redirect:/board/" + reply.getCommentId();
    }

    // 댓글 삭제
    @GetMapping("/{replyId}/delete")
    public String delete(@PathVariable long replyId) {
        replyService.deleteReply(replyId);
        return "redirect:/board"; // 삭제 후 목록 페이지로 리다이렉트
    }
}
