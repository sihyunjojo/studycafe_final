package project.studycafe.repository.board.board;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.board.AttachmentFile;
import project.studycafe.domain.board.Board;

import java.util.List;



public interface JpaBoardRepository extends JpaRepository<Board, Long> { ;
    List<Board> findAllByCategoryNotOrderByCreatedTimeDesc(String category);
    List<Board> findAllByCategoryOrderByCreatedTimeDesc(String category);
    //Containing은 해당 리스트 필드에서 포함되는 값을 찾는 조건을 나타냅니다.
    List<Board> findALlByAttachmentFiles(AttachmentFile File);

    //contains는 문자열을 기준으로 해당 필드가 특정 문자열을 포함하는지 확인하는 조건을 나타냅니다.
}
