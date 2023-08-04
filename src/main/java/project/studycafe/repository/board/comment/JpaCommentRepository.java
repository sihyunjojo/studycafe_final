package project.studycafe.repository.board.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.board.Comment;

import java.util.List;

public interface JpaCommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
    }
