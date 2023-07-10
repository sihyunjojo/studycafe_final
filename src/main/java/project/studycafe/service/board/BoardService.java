package project.studycafe.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.BoardCreateForm;
import project.studycafe.contoller.form.BoardForm;
import project.studycafe.domain.Board;
import project.studycafe.repository.board.board.JpaQueryBoardRepository;
import project.studycafe.repository.board.board.JpaBoardRepository;
import project.studycafe.repository.board.board.dto.BoardSearchCond;
import project.studycafe.repository.member.JpaMemberRepository;

import java.util.ArrayList;
import java.util.Iterator;
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
        }
        else{
            homeBoards.addAll(0, notices);
        }

        return homeBoards;
    }

    public List<Board> getBoardList(int page, int perPageNum, List<Board> boards) { // 현재페이지, 페이지당 몇개를 보여주는지
        int startBoard = (page - 1) * perPageNum;
        int endBoard = Math.min(page * perPageNum, boards.size());

        return boards.subList(startBoard, endBoard);
    }

    public List<Board> getBoardsByCategoryNotOrderByCreatedTimeDesc(String category) {
        return boardRepository.findAllByCategoryNotOrderByCreatedTimeDesc(category);
    }

    public List<Board> getBoardsByCategoryByCreatedTimeDesc(String category) {
        return boardRepository.findAllByCategoryOrderByCreatedTimeDesc(category);
    }


    private void boardsToUpNoticeBoards(List<Board> boards) {
        List<Board> notices = new ArrayList<>();

        Iterator<Board> iterator = boards.iterator(); // 이걸 사용하지 않으면 반복문이 돌면서 사라지는 것때문에 오류 일으킴.
        while (iterator.hasNext()) {
            Board board = iterator.next();
            if (board.getCategory().equals("공지사항")) {
                iterator.remove();
                notices.add(notices.size(), board);
            }
        }
        boards.addAll(0, notices);
    }

    //boards에서 사용
    public List<Board> getSearchedAndSortedBoards(BoardSearchCond cond) {
        return boardQueryRepository.findSearchedAndSortedBoards(cond);
    }


    public void addBoard(BoardCreateForm form) {
        Board board = new Board();
        board.setMember(memberRepository.findById(form.getMemberId()).orElseThrow());
        board.setTitle(form.getTitle());
        board.setCategory(form.getCategory());
        board.setContent(form.getContent());
        board.setAttachmentFiles(form.getAttachmentFiles());

    }

    public Optional<Board> findById(long boardId) {
        return boardRepository.findById(boardId);
    }

    public void updateBoard(Long boardId, BoardCreateForm boardForm) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        board.setTitle(boardForm.getTitle());
        board.setContent(boardForm.getContent());
        board.setCategory(boardForm.getCategory());
        board.setAttachmentFiles(boardForm.getAttachmentFiles());
    }

    public void deleteBoard(long boardId) {
        boardRepository.deleteById(boardId);
    }

    public void increaseReadCount(Board board) {
        board.setReadCount(board.getReadCount() + 1);
    }

    public void increaseLikeCount(Board board) {
        board.setLikeCount(board.getLikeCount() + 1);
    }

    public void decreaseLikeCount(Board board) {
        board.setLikeCount(board.getLikeCount() - 1);
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
