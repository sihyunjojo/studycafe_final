package project.studycafe.app.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.studycafe.app.controller.form.board.BoardCreateForm;
import project.studycafe.app.controller.form.board.BoardForm;
import project.studycafe.app.controller.form.board.BoardUpdateForm;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.board.Info.BoardBaseInfo;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static project.studycafe.app.controller.form.board.AttachmentFileForm.createAttachmentFileForms;
import static project.studycafe.app.domain.board.Board.createBoard;
import static project.studycafe.app.domain.board.Info.BoardBaseInfo.createBoardInfo;

@Slf4j
//어떤 효과인지 찾아보기
@Transactional
@SpringBootTest
@RequiredArgsConstructor
class BoardServiceTest {

    private final BoardService boardService;
    private final FileService fileService;


    Member member;
    Board board;

    // 우선 테스트 DB를 따로 만들어야함.
//    일관된 상태 유지: 테스트가 실행될 때 데이터베이스 상태가 일관된 상태로 유지됩니다.
//    이는 데이터베이스 작업이 트랜잭션 내에서 모두 처리되기 때문에 발생합니다.
    @BeforeEach
    void beforeEach() {
        //when
        member = new Member(100L, "a", "a@google.com", "google", "hello");

        BoardBaseInfo boardInfo = createBoardInfo("공지사항", "공지사항", "공지사항");
        board = createBoard(member, boardInfo);
    }

    // 우선 테스트 DB를 따로 만들어야함.
//    롤백 지원: 테스트에서 트랜잭션을 사용하면 기본적으로 각 테스트 후에 롤백됩니다.
//    따라서 테스트에서 데이터베이스 상태를 변경하더라도 실제 데이터베이스에 영향을 미치지 않고 테스트 간에 독립성을 유지할 수 있습니다.
//    @AfterEach
//    void afterEach() {
//        boardService.clear();
//    }


    @Test
    @DisplayName("게시판 생성 테스트")
    void addBoard() {

        //given
        BoardCreateForm boardCreateForm = new BoardCreateForm(member.getId(), "공지사항", "공지사항", "공지사항");
        Long boardId = boardService.addBoard(boardCreateForm);

        //then
        Optional<Board> findBoard = boardService.findById(boardId);
        assertThat(findBoard.orElseThrow()).isEqualTo(board);

    }

    @Test
    @DisplayName("게시판 업데이트 테스트")
    void updateBoard() {
        //given
        BoardUpdateForm boardUpdateForm = new BoardUpdateForm("공지사항1", "공지사항", "공지사항1");

        boardService.updateBoard(board.getId(), boardUpdateForm);

        //then
        Optional<Board> findBoard = boardService.findById(board.getId());

        assertThat((String) findBoard.orElseThrow().toMap().get("title")).isEqualTo(boardUpdateForm.getTitle());
        assertThat((String) findBoard.orElseThrow().toMap().get("category")).isEqualTo(boardUpdateForm.getCategory());
        assertThat((String) findBoard.orElseThrow().toMap().get("content")).isEqualTo(boardUpdateForm.getContent());
    }

    @Test
    @DisplayName("게시판 삭제 테스트")
    void deleteBoard() {
        //given
        Long boardId = board.getId();

        boardService.deleteBoard(boardId);

        //then
        assertThat(boardService.findById(boardId)).isEmpty();
    }

    @Test
    @DisplayName("게시판 조회수 증가 테스트")
    void increaseReadCount() {
        //given
        boardService.increaseReadCount(board);

        //then
        assertThat((Long) board.toMap().get("readCount")).isEqualTo(1L);
    }

    // 우선 테스트 DB를 따로 만들어야함.
    @Test
    @DisplayName("받아온 게시판 엔티티 form 형태로 변경 테스트")
    void boardToBoardForm() {
        //when
        List<MultipartFile> multipartFileList = new ArrayList<>();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                "test image data".getBytes()
        );

        multipartFileList.add(multipartFile);

        fileService.storeFiles(multipartFileList, board.getId());

        //given
        BoardForm boardForm = boardService.boardToBoardForm(board);


        //then
        assertThat((Long) board.toMap().get("id")).isEqualTo(boardForm.getId());
        assertThat((Long) board.toMap().get("memberId")).isEqualTo(boardForm.getMemberId());
        assertThat((String) board.toMap().get("title")).isEqualTo(boardForm.getTitle());
        assertThat((String) board.toMap().get("category")).isEqualTo(boardForm.getCategory());
        assertThat((String) board.toMap().get("content")).isEqualTo(boardForm.getContent());

        assertThat(createAttachmentFileForms((List<AttachmentFile>) board.toMap().get("attachmentFiles"))).isEqualTo(boardForm.getAttachmentFiles());


//        assertThat(multipartFile)
//                .isNotNull()
//                .hasFieldOrPropertyWithValue("originalFilename", "test-image.png")
//                .hasFieldOrPropertyWithValue("board.id", board.getId());

    }
}