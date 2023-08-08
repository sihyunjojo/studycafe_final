package project.studycafe.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.board.AttachmentFile;
import project.studycafe.domain.board.Board;
import project.studycafe.domain.board.Info.BoardBaseInfo;
import project.studycafe.domain.form.board.AttachmentFileForm;
import project.studycafe.domain.form.board.BoardCreateForm;
import project.studycafe.domain.form.board.BoardForm;
import project.studycafe.domain.form.board.BoardUpdateForm;
import project.studycafe.domain.member.Member;
import project.studycafe.repository.board.board.JpaQueryBoardRepository;
import project.studycafe.repository.board.board.JpaBoardRepository;
import project.studycafe.domain.form.search.BoardSearchCond;
import project.studycafe.repository.member.JpaMemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static project.studycafe.domain.board.QBoard.board;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final JpaBoardRepository boardRepository;
    private final JpaMemberRepository memberRepository;
    private final JpaQueryBoardRepository boardQueryRepository;


    public List<Board> getHomeBoards() {
        List<Board> boardsWithoutNotice = boardRepository.findAllByBoardBaseInfo_CategoryNotOrderByCreatedTimeDesc("공지사항");
        List<Board> notices = boardRepository.findAllByBoardBaseInfo_CategoryOrderByCreatedTimeDesc("공지사항");
        List<Board> homeBoards = boardsWithoutNotice;

        if (notices.size() > 3) {
            List<Board> Top3Notices = notices.subList(0, 3);
            homeBoards.addAll(0, Top3Notices);
        } else {
            homeBoards.addAll(0, notices);
        }

        return homeBoards;
    }

    public List<Board> getBoardList(int page, int perPageNum, List<Board> boards) { // 현재페이지, 페이지당 몇개를 보여주는지
        int startBoard = (page - 1) * perPageNum;
        int endBoard = Math.min(page * perPageNum, boards.size());

        return boards.subList(startBoard, endBoard);
    }

    public Long addBoard(BoardCreateForm form) {
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        BoardBaseInfo boardBaseInfo = BoardBaseInfo.createBoardInfo(form.getTitle(), form.getCategory(), form.getContent());
        Board board = Board.createBoard(member, boardBaseInfo);

        log.info("save board ={}", board);

        boardRepository.save(board);

        return board.getId();
    }

    public void updateBoard(Long boardId, BoardUpdateForm form) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        BoardBaseInfo boardBaseInfo = BoardBaseInfo.createBoardInfo(form.getTitle(), form.getCategory(), form.getContent());
        board.updateBoardBaseInfo(boardBaseInfo);
    }

    public void deleteBoard(Long boardId) {
        log.info("board id = {}", boardId);
        log.info("board = {}", boardRepository.findById(boardId));
        boardRepository.deleteById(boardId);
    }

    public Optional<Board> findById(long boardId) {
        return boardRepository.findById(boardId);
    }

    public List<Board> getSearchedAndSortedBoards(BoardSearchCond cond) {
        return boardQueryRepository.findSearchedAndSortedBoards(cond);
    }

    public List<Board> getBoardsByCategoryNotOrderByCreatedTimeDesc(String category) {
        return boardRepository.findAllByBoardBaseInfo_CategoryNotOrderByCreatedTimeDesc(category);
    }

    public List<Board> getBoardsByCategoryByCreatedTimeDesc(String category) {
        return boardRepository.findAllByBoardBaseInfo_CategoryOrderByCreatedTimeDesc(category);
    }

    public void increaseReadCount(Board board) {
        board.upReadCount();
    }

    public void upLikeCountBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.upLikeCount();
        findBoard.downReadCount();
    }

    public void downLikeCountBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.downLikeCount();
        findBoard.downReadCount();
    }

    public List<BoardForm> boardsToBoardForms(List<Board> boards) {
        return boards.stream()
                .map(Board::toMap)
                .map(boardMap -> new BoardForm(
                        (Long) boardMap.get("id"),
                        ((Member) boardMap.get("member")).getNickname(),
                        ((Member) boardMap.get("member")).getName(),
                        (String) boardMap.get("title"),
                        (String) boardMap.get("category"),
                        (String) boardMap.get("content"),
                        (LocalDateTime) boardMap.get("createdTime"),
                        (List<AttachmentFileForm>) boardMap.get("attachmentFiles"),
                        (Integer) boardMap.get("readCount"),
                        (Integer) boardMap.get("likeCount")
                ))
                .collect(Collectors.toList());
    }

    public BoardForm boardToBoardForm(Board board) {

        Map<String, Object> boardMap = board.toMap();

        log.info(boardMap.get("attachmentFiles").toString());
        return new BoardForm(
                (Long) boardMap.get("id"),
                ((Member) boardMap.get("member")).getNickname(),
                ((Member) boardMap.get("member")).getName(),
                (String) boardMap.get("title"),
                (String) boardMap.get("category"),
                (String) boardMap.get("content"),
                (LocalDateTime) boardMap.get("createdTime"),
                AttachmentFileForm.createAttachmentFileForms((List<AttachmentFile>) boardMap.get("attachmentFiles")),
                (Integer) boardMap.get("readCount"),
                (Integer) boardMap.get("likeCount")
        );

//        return new BoardForm(board.getId(), board.getMember().getNickname(), board.getMember().getName(), board.getTitle(), board.getCategory(), board.getContent(),
//                board.getCreatedTime(), board.getAttachmentFiles(), board.getPopup(), board.getReadCount(), board.getLikeCount());

    }
}
