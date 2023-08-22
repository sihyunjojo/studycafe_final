package project.studycafe.app.repository.board.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.board.Comment;

import java.util.List;

public interface JpaCommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoard_Id(Long boardId);
    void deleteByBoardId(Long boardId);
    }
