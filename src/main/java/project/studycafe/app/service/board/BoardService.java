package project.studycafe.app.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.board.*;
import project.studycafe.app.repository.board.comment.JpaCommentRepository;
import project.studycafe.app.service.dto.searchDto.BoardSearchCond;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.board.Info.BoardBaseInfo;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.repository.board.board.JpaQueryBoardRepository;
import project.studycafe.app.repository.board.board.JpaBoardRepository;
import project.studycafe.app.repository.member.JpaMemberRepository;
import project.studycafe.helper.exception.member.NotFoundMemberException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final JpaBoardRepository boardRepository;
    private final JpaQueryBoardRepository boardQueryRepository;
    private final JpaMemberRepository memberRepository;



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
        Optional<Member> findMember = memberRepository.findById(form.getMemberId());
        Member member = findMember.orElseThrow(() -> {
                    log.error("찾는 회원 아이디 = " + form.getMemberId());
                    return new NotFoundMemberException("회원을 찾을 수 없습니다.");
                }
            );

        BoardBaseInfo boardBaseInfo = BoardBaseInfo.createBoardInfo(form.getTitle(), form.getCategory(), form.getContent());

        Board board = Board.createBoard(member, boardBaseInfo);

        boardRepository.save(board);

        return board.getId();
    }

    public void updateBoard(Long boardId, BoardUpdateForm form) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        BoardBaseInfo boardBaseInfo = BoardBaseInfo.createBoardInfo(form.getTitle(), form.getCategory(), form.getContent());
        board.updateBoardBaseInfo(boardBaseInfo);
    }

    public void deleteBoard(Long boardId) {
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

    public void increaseReadCount(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.upReadCount();
    }

    public void upLikeCountBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.upLikeCount();
        findBoard.upReadCount();
    }

    public void downLikeCountBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        findBoard.downLikeCount();
        findBoard.upReadCount();
    }

    public Board getBoardWithMemberCommentReplyAttachmentFile(long boardId){
        Board board = boardQueryRepository.findByIdWithMemberByQuery(boardId);
//        List<Comment> Comment= jpaCommentRepository.findAllByBoard_Id(boardId);
        return board;

    }

    public BoardForm boardToBoardForm(Board board) {
        Map<String, Object> boardMap = board.toMap();
        return BoardForm.builder()
                .id((Long) boardMap.get("id"))
                .memberId((Long) boardMap.get("memberId"))
                .memberName((String) boardMap.get("memberName"))
                .memberNickname((String) boardMap.get("memberNickname"))
                .title((String) boardMap.get("title"))
                .category((String) boardMap.get("category"))
                .content((String) boardMap.get("content"))
                .createdTime((LocalDateTime) boardMap.get("createdTime"))
                .attachmentFiles(AttachmentFileForm.createAttachmentFileForms(
                        (List<AttachmentFile>) boardMap.get("attachmentFiles")))
                .comments(CommentForm.createCommentForms(
                        (List<Comment>) boardMap.get("comments")))
                .likeCount((Integer) boardMap.get("likeCount"))
                .readCount((Integer) boardMap.get("readCount")).build();
    }

    public List<BoardForm> boardsToBoardForms(List<Board> boards) {
        return boards.stream()
                .map(Board::toMap)
                .map(boardMap -> new BoardForm(
                        (Long) boardMap.get("id"),
                        (Long) boardMap.get("memberId"),
                        (String) boardMap.get("memberName"),
                        (String) boardMap.get("memberNickname"),
                        (String) boardMap.get("title"),
                        (String) boardMap.get("category"),
                        (String) boardMap.get("content"),
                        (LocalDateTime) boardMap.get("createdTime"),
                        new ArrayList<>(),
                        new ArrayList<>(),
//                        AttachmentFileForm.createAttachmentFileForms((List<AttachmentFile>) boardMap.get("attachmentFiles")),
//                        CommentForm.createCommentForms((List<Comment>) boardMap.get("comments")),
                        (Integer) boardMap.get("readCount"),
                        (Integer) boardMap.get("likeCount")
                ))
                .collect(Collectors.toList());
    }


    public void clear(){
        boardRepository.deleteAll();
    }
}
