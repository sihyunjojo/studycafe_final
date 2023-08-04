package project.studycafe.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.board.Board;
import project.studycafe.domain.form.board.BoardCreateForm;
import project.studycafe.domain.form.board.BoardForm;
import project.studycafe.domain.form.board.BoardUpdateForm;
import project.studycafe.domain.member.Member;
import project.studycafe.repository.board.board.JpaQueryBoardRepository;
import project.studycafe.repository.board.board.JpaBoardRepository;
import project.studycafe.domain.form.search.BoardSearchCond;
import project.studycafe.repository.member.JpaMemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final JpaBoardRepository boardRepository;
    private final JpaMemberRepository memberRepository;
    private final JpaQueryBoardRepository boardQueryRepository;


    public List<Board> getHomeBoards() {
        List<Board> boardsWithoutNotice = boardRepository.findAllByCategoryNotOrderByCreatedTimeDesc("공지사항");
        List<Board> notices = boardRepository.findAllByCategoryOrderByCreatedTimeDesc("공지사항");
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
        Board board = new Board();
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        board.setMember(member);
        board.setTitle(form.getTitle());
        board.setCategory(form.getCategory());
        board.setContent(form.getContent());
        log.info("save board ={}", board);

        boardRepository.save(board);

        return board.getId();
    }

    public void updateBoard(Long boardId, BoardUpdateForm form) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setCategory(form.getCategory());
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
        return boardRepository.findAllByCategoryNotOrderByCreatedTimeDesc(category);
    }

    public List<Board> getBoardsByCategoryByCreatedTimeDesc(String category) {
        return boardRepository.findAllByCategoryOrderByCreatedTimeDesc(category);
    }

    public void increaseReadCount(Board board) {
        board.setReadCount(board.getReadCount() + 1);
    }

    public void upLikeCountBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.setLikeCount(findBoard.getLikeCount() + 1);
        if (findBoard.getReadCount() > 0) {
            findBoard.setReadCount(findBoard.getReadCount() - 1);
        }
    }

    public void downLikeCountBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        if (findBoard.getLikeCount() > 0) {
            findBoard.setLikeCount(findBoard.getLikeCount() - 1);
        }
        if (findBoard.getReadCount() > 0) {
            findBoard.setReadCount(findBoard.getReadCount() - 1);
        }
    }

    public List<BoardForm> boardsToBoardForms(List<Board> boards) {
        return boards.stream()
                .map(b -> new BoardForm(b.getId(), b.getMember().getNickname(), b.getMember().getName(), b.getTitle(), b.getCategory(), b.getContent(),
                        b.getCreatedTime(), b.getAttachmentFiles(), b.getPopup(), b.getReadCount(), b.getLikeCount()))
                .collect(Collectors.toList());
    }

    public BoardForm boardToBoardForm(Board board) {
        return new BoardForm(board.getId(), board.getMember().getNickname(), board.getMember().getName(), board.getTitle(), board.getCategory(), board.getContent(),
                board.getCreatedTime(), board.getAttachmentFiles(), board.getPopup(), board.getReadCount(), board.getLikeCount());

    }
}
