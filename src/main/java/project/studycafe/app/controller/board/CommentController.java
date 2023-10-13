package project.studycafe.app.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.app.controller.form.board.CommentForm;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.member.Member;
import project.studycafe.helper.resolver.argumentresolver.Login;
import project.studycafe.app.service.board.BoardService;
import project.studycafe.app.service.board.CommentService;

import java.util.List;
import java.util.Optional;

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
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/board/" + form.getBoardId();
        }

        commentService.addComment(form);
        return "redirect:/board/" + form.getBoardId();
    }

    // 댓글 수정
    @PostMapping("/{commentId}/edit")
    public String edit(CommentForm comment, @PathVariable Long commentId, @RequestParam Long boardId) {
        commentService.editComment(commentId, comment);
        return "redirect:/board/" + boardId;
    }


    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String delete(@PathVariable long commentId, @RequestParam Long boardId, Model model) {
        Optional<Comment> comment = commentService.findById(commentId);
        model.addAttribute("comment", comment.orElseThrow());

        commentService.deleteComment(commentId);
        return "redirect:/board/" + boardId; // 삭제 후 목록 페이지로 리다이렉트
    }
}
