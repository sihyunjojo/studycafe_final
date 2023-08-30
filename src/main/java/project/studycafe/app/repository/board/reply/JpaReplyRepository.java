package project.studycafe.app.repository.board.reply;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.studycafe.app.domain.board.Reply;

import java.util.List;

public interface JpaReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long commentId);
    List<Reply> getRepliesByCommentId(Long commentId);
}
