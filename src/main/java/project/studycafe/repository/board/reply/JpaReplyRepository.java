package project.studycafe.repository.board.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.Reply;

import java.util.List;

public interface JpaReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long commentId);
    List<Reply> getRepliesByCommentId(Long commentId);
}
