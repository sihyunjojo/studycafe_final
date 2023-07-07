package project.studycafe.service.board;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.Comment;
import project.studycafe.domain.Reply;
import project.studycafe.repository.board.comment.JpaCommentRepository;
import project.studycafe.repository.board.reply.JpaReplyRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final JpaReplyRepository replyRepository;

    public List<Reply> findReplys() {
        return replyRepository.findAll();
    }


    public void addReply(Reply reply) {
        replyRepository.save(reply);
    }

    public void editReply(Long replyId, Reply updateReply) {
        Reply findReply = replyRepository.findById(replyId).orElseThrow();

        findReply.setContent(updateReply.getContent());
    }


    public void deleteReply(long replyId) {
        replyRepository.deleteById(replyId);
    }

    public List<Reply> findByCommentId(Long commentId) {
        return replyRepository.findByCommentId(commentId);
    }

    public List<Reply> getRepliesByCommentId(Long commentId) {
        return replyRepository.getRepliesByCommentId(commentId);
    }
}
