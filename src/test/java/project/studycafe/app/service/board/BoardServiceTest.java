package project.studycafe.app.service.board;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.studycafe.app.controller.form.board.BoardCreateForm;
import project.studycafe.app.controller.form.board.BoardForm;
import project.studycafe.app.controller.form.board.BoardUpdateForm;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.FileService;
import project.studycafe.app.service.member.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static project.studycafe.app.controller.form.board.AttachmentFileForm.createAttachmentFileForms;

@Slf4j
//어떤 효과인지 찾아보기
@Transactional
@SpringBootTest
//@ActiveProfiles("test") //프로파일을 지정할 때 사용됩니다. application-{profile}.properties
//@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"})
class BoardServiceTest {
    @Autowired MemberService memberService;
    @Autowired BoardService boardService;
    @Autowired FileService fileService;

    Member member;
    Long memberId;

    Long boardId;
    Board board;

    // 우선 테스트 DB를 따로 만들어야함.
    @BeforeEach
    void beforeEach() {
        // 기본으로 member을 만들어서 제공헤주고
        //given
        CommonMemberForm commonMemberForm = new CommonMemberForm("1", "1", "1", "1", "01085524018");
        memberId = memberService.join(commonMemberForm);

        Optional<Member> findMember = memberService.findById(memberId);
        member = findMember.orElseThrow();

        // 기본으로 board 를 만들어서 제공해줌.
        BoardCreateForm boardCreateForm= new BoardCreateForm(memberId, "공지사항", "공지사항", "공지사항");

        //when
        boardId = boardService.addBoard(boardCreateForm);
        board = boardService.findById(boardId).orElseThrow();

        log.info("beforeEach 끝");
    }

    // 우선 테스트 DB를 따로 만들어야함.
//    롤백 지원: 테스트에서 트랜잭션을 사용하면 기본적으로 각 테스트 후에 롤백됩니다.
//    따라서 테스트에서 데이터베이스 상태를 변경하더라도 실제 데이터베이스에 영향을 미치지 않고 테스트 간에 독립성을 유지할 수 있습니다.
//    @AfterEach
//    void afterEach() {
//        boardService.clear();
//    }

    @Test
    @DisplayName("초기화")
    @Order(1)
    void hm() {
    }

    @Test
    @DisplayName("게시판 생성 테스트")
    void addBoard() {

        //given
        //when
        //Before Each에서 모든 것 제공

        //then
        assertThat(board.getId()).isEqualTo(boardId);
    }

    @Test
    @DisplayName("게시판 업데이트 테스트")
    void updateBoard() {
        //given
        BoardUpdateForm boardUpdateForm = new BoardUpdateForm("공지사항1", "공지사항", "공지사항1");

        //when
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
        //when
        boardService.deleteBoard(boardId);

        //then
        assertThat(boardService.findById(boardId)).isEmpty();
    }

    @Test
    @DisplayName("게시판 조회수 증가 테스트")
    void increaseReadCount() {
        //given
        Integer readCount = board.getReadCount();

        //when
        boardService.increaseReadCount(boardId);
        Integer updateReadCount = board.getReadCount();

        //then
        assertThat(readCount + 1).isEqualTo(updateReadCount);
    }

    @Test
    @DisplayName("게시판 좋아요 증가 테스트")
    void increaseLikeCount() {
        //given
        Integer readCount = board.getReadCount();
        Integer likeCount = board.getLikeCount();

        //when
        boardService.upLikeCountBoard(boardId);
        Integer updateReadCount = board.getReadCount();
        Integer updateLikeCount = board.getLikeCount();

        //then
        assertThat(readCount + 1).isEqualTo(updateReadCount);
        assertThat(likeCount + 1).isEqualTo(updateLikeCount);
    }

    @Test
    @DisplayName("게시판 좋아요 2일시, 좋아요 감소 테스트")
    void decreaseLikeCountWhen2Like() {
        //given
        Integer readCount = board.getReadCount();
        Integer likeCount = board.getLikeCount();
        boardService.upLikeCountBoard(boardId);
        boardService.upLikeCountBoard(boardId);


        //when
        boardService.downLikeCountBoard(boardId);
        Integer updateReadCount = board.getReadCount();
        Integer updateLikeCount = board.getLikeCount();

        //then
        assertThat(readCount + 3).isEqualTo(updateReadCount);
        assertThat(likeCount + 2 - 1).isEqualTo(updateLikeCount);
    }

    @Test
    @DisplayName("게시판 좋아요 0일시, 좋아요 감소 테스트")
    void decreaseLikeCountWhen0Like() {
        //given
        Integer readCount = board.getReadCount();

        //when
        boardService.downLikeCountBoard(boardId);
        Integer updateReadCount = board.getReadCount();
        Integer updateLikeCount = board.getLikeCount();

        //then
        assertThat(readCount + 1).isEqualTo(updateReadCount);
        assertThat(0).isEqualTo(updateLikeCount);
    }

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