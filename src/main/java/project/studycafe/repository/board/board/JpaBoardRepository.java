package project.studycafe.repository.board.board;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.Board;

import java.util.List;



public interface JpaBoardRepository extends JpaRepository<Board, Long> { ;
    List<Board> findAllByCategoryNotOrderByCreatedTimeDesc(String category);
    List<Board> findAllByCategoryOrderByCreatedTimeDesc(String category);


}
