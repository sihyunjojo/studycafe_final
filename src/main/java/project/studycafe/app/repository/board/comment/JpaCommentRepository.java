package project.studycafe.app.repository.board.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.board.Reply;

import java.util.List;
import java.util.Optional;

public interface JpaCommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentByRepliesContaining(Reply reply);
}
