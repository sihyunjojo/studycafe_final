package project.studycafe.service.board;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.board.Comment;
import project.studycafe.domain.board.Reply;
import project.studycafe.domain.form.board.ReplyForm;
import project.studycafe.domain.member.Member;
import project.studycafe.repository.board.comment.JpaCommentRepository;
import project.studycafe.repository.board.reply.JpaReplyRepository;
import project.studycafe.repository.member.JpaMemberRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final JpaReplyRepository replyRepository;
    private final JpaMemberRepository memberRepository;
    private final JpaCommentRepository commentRepository;

    public List<Reply> findReplys() {
        return replyRepository.findAll();
    }


    public void addReply(ReplyForm form) {
        Comment comment = commentRepository.findById(form.getCommentId()).orElseThrow();
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        Reply reply = Reply.createReply(member, comment, form.getContent());

        replyRepository.save(reply);
    }

    public void editReply(Long replyId, ReplyForm updateReply) {
        Reply findReply = replyRepository.findById(replyId).orElseThrow();
        findReply.updateReply(updateReply.getContent());
    }


    public void deleteReply(long replyId) {
        replyRepository.deleteById(replyId);
    }

    public List<Reply> getRepliesByCommentId(Long commentId) {
        return replyRepository.getRepliesByCommentId(commentId);
    }
}
