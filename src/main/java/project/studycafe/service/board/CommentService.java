package project.studycafe.service.board;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.CommentForm;
import project.studycafe.domain.Board;
import project.studycafe.domain.Comment;
import project.studycafe.repository.board.board.JpaBoardRepository;
import project.studycafe.repository.board.comment.JpaCommentRepository;
import project.studycafe.repository.member.JpaMemberRepository;

import java.time.LocalDateTime;
import java.util.List;

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
        Comment comment = new Comment();
        comment.setBoard(boardRepository.findById(form.getBoardId()).orElseThrow());
        comment.setMember(memberRepository.findById(form.getMemberId()).orElseThrow());
        comment.setContent(form.getContent());
        commentRepository.save(comment);
    }

    public void editComment(Long commentId, Comment updateComment) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow();
        findComment.setContent(updateComment.getContent());
    }


    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }


    public List<Comment> findByBoardId(Long boardId) {
        return  commentRepository.findByBoardId(boardId);
    }
}
