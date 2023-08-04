package project.studycafe.service.board;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.board.Reply;
import project.studycafe.domain.form.board.ReplyForm;
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
        Reply reply = new Reply();
        reply.setComment(commentRepository.findById(form.getCommentId()).orElseThrow());
        reply.setMember(memberRepository.findById(form.getMemberId()).orElseThrow());
        reply.setContent(form.getContent());
        replyRepository.save(reply);
    }

    public void editReply(Long replyId, Reply updateReply) {
        Reply findReply = replyRepository.findById(replyId).orElseThrow();
        findReply.setContent(updateReply.getContent());
    }


    public void deleteReply(long replyId) {
        replyRepository.deleteById(replyId);
    }

    public List<Reply> getRepliesByCommentId(Long commentId) {
        return replyRepository.getRepliesByCommentId(commentId);
    }
}
