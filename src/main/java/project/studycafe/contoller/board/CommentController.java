package project.studycafe.contoller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.domain.board.Comment;
import project.studycafe.domain.form.board.CommentForm;
import project.studycafe.domain.member.Member;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.service.board.BoardService;
import project.studycafe.service.board.CommentService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/{boardId}")
    public String comments(@PathVariable long boardId, Model model) {
        List<Comment> comments = commentService.findComments();
        return "board/board";
    }

    // 댓글 생성
    @PostMapping()
    public String add(@Login Member loginMember, CommentForm form) {
        log.info("loginMember = {}", loginMember);
        log.info("commentForm = {}", form);
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/board/" + form.getBoardId();
        }

        commentService.addComment(form);
        log.info("success");
        return "redirect:/board/" + form.getBoardId();
    }

    // 댓글 수정
    @PostMapping("/{commentId}/edit")
    public String edit(Comment comment, @PathVariable Long commentId) {
        commentService.editComment(commentId, comment);
        return "redirect:/board/" + comment.getBoard().getId();
    }


    // 댓글 삭제
    @GetMapping("/{commentId}/delete")
    public String delete(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/board"; // 삭제 후 목록 페이지로 리다이렉트
    }
}
