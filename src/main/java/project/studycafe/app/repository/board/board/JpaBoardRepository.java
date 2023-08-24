package project.studycafe.app.repository.board.board;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;

import java.util.List;



public interface JpaBoardRepository extends JpaRepository<Board, Long> { ;

    // 2개이상 사용하는거 불가능.
    @EntityGraph(attributePaths = "member", type = EntityGraph.EntityGraphType.LOAD, value = "Board.withMember")
    List<Board> findAllByBoardBaseInfo_CategoryNotOrderByCreatedTimeDesc(String category);

    @EntityGraph(attributePaths = "member", type = EntityGraph.EntityGraphType.LOAD, value = "Board.withMember")
    List<Board> findAllByBoardBaseInfo_CategoryOrderByCreatedTimeDesc(String category);

    @EntityGraph(attributePaths = "member", type = EntityGraph.EntityGraphType.LOAD, value = "Board.withMember")
    List<Board> findALlByBoardAddInfo_AttachmentFiles(AttachmentFile File);

    //Containing은 해당 리스트 필드에서 포함되는 값을 찾는 조건을 나타냅니다.
    //contains는 문자열을 기준으로 해당 필드가 특정 문자열을 포함하는지 확인하는 조건을 나타냅니다.
}
