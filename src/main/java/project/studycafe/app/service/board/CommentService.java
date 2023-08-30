package project.studycafe.app.service.board;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.controller.form.board.CommentForm;
import project.studycafe.app.domain.board.Reply;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.repository.board.board.JpaBoardRepository;
import project.studycafe.app.repository.board.comment.JpaCommentRepository;
import project.studycafe.app.repository.member.JpaMemberRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final JpaCommentRepository commentRepository;
    private final JpaMemberRepository memberRepository;
    private final JpaBoardRepository boardRepository;

    public List<Comment> findComments() {
        return commentRepository.findAll();
    }


    public void addComment(CommentForm form) {
        Board board = boardRepository.findById(form.getBoardId()).orElseThrow();
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        Comment comment = Comment.createComment(member, board, form.getContent());

        commentRepository.save(comment);
    }

    public void editComment(Long commentId, CommentForm updateComment) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow();
        findComment.updateComment(updateComment.getContent());
    }

    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Optional<Comment> findById(long commentId) {
        return commentRepository.findById(commentId);
    }

    public Optional<Comment> findCommentByRepliesContaining(Reply reply) {
        log.info("findCommentByRepliesContaining = {}", commentRepository.findCommentByRepliesContaining(reply));
        return commentRepository.findCommentByRepliesContaining(reply);
    }

}
