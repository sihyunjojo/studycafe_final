package project.studycafe.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.contoller.form.BoardCreateForm;
import project.studycafe.contoller.form.BoardForm;
import project.studycafe.domain.*;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.repository.board.board.dto.BoardSearchCond;
import project.studycafe.service.board.BoardService;
import project.studycafe.service.board.CommentService;
import project.studycafe.service.board.ReplyService;

import java.util.List;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final ReplyService replyService;

    private static final int BASIC_PER_PAGE_NUM = 10;    // 페이지당 보여줄 게시판 개수

    @GetMapping()
    public String boards(@ModelAttribute("boardSearch") BoardSearchCond boardSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Board> boardsWithoutNotice = boardService.getBoardsByCategoryNotOrderByCreatedTimeDesc("공지사항");
        List<Board> notices = boardService.getBoardsByCategoryByCreatedTimeDesc("공지사항");
        boardSearch.setPerPageNum(BASIC_PER_PAGE_NUM);

        List<Board> boardList = boardService.getBoardList(page, BASIC_PER_PAGE_NUM, boardsWithoutNotice);
        boardList.addAll(0, notices);

        PageMaker pageMaker = new PageMaker(boardsWithoutNotice.size(), page, BASIC_PER_PAGE_NUM);

        log.info("Boards = {}", boardList);

        // 클라이언트 처리
        List<BoardForm> boardForms = boardService.boardsToBoardForms(boardList);
        model.addAttribute("boards", boardForms);
        model.addAttribute("pageMaker", pageMaker);

        log.info("Boards = {}", boardForms);
        return "board/boards";
    }

    @GetMapping("/search")
    public String searchBoards(@ModelAttribute("boardSearch") BoardSearchCond boardSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Board> findBoards = boardService.getSearchedAndSortedBoards(boardSearch);
        List<Board> notices = boardService.getBoardsByCategoryByCreatedTimeDesc("공지사항");

        log.info("search={}", boardSearch);

        List<Board> findBoardList = boardService.getBoardList(page, boardSearch.getPerPageNum(), findBoards);

        // 공지사항을 검색했을 시에 2번 안 띄우게
        if (!boardSearch.getCategory().equals("공지사항")) {
            findBoardList.addAll(0, notices);
        }

        PageMaker pageMaker = new PageMaker(findBoards.size(), page, boardSearch.getPerPageNum());

        // 클라이언트 처리
        List<BoardForm> boardForms = boardService.boardsToBoardForms(findBoardList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("boards", boardForms);
        model.addAttribute("boardSearch", boardSearch);

        return "board/boards";
    }

    @GetMapping("/{boardId}")
    public String board(@PathVariable long boardId, Model model) {
        Board board = boardService.findById(boardId).orElseThrow();
        boardService.increaseReadCount(board);
        BoardForm boardForm = boardService.boardToBoardForm(board);

        List<Comment> comments = commentService.findByBoardId(boardId);

//        List<Reply> AllReplys = new ArrayList<>();
        for (Comment comment : comments) {
            List<Reply> replies = replyService.getRepliesByCommentId(comment.getId()); // 해당 댓글에 대한 답변 목록 조회
            comment.setReplies(replies); // 댓글 객체에 답변 목록 설정
        }

        model.addAttribute("board", boardForm);
        model.addAttribute("comments", comments);
        return "board/board";
    }

    @GetMapping("/add")
    public String addForm(@Login Member loginMember, Model model) {
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/board/add";
        }
        model.addAttribute("board", new BoardCreateForm());
        model.addAttribute(LOGIN_MEMBER, loginMember);
        return "board/addBoardForm";
    }

    @PostMapping("/add")
    public String add(@Login Member loginMember, BoardCreateForm form) {
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/board/add";
        }

        log.info("boardForm ={}", form);
        log.info("loginMember={}", loginMember);

        boardService.addBoard(form);
        return "redirect:/board"; // 일단 home으로 보내주자 나중에 board목록으로 보내주고
    }

    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId).orElseThrow();
        BoardForm boardForm = boardService.boardToBoardForm(board);

        model.addAttribute("board", boardForm);
        return "board/editBoardForm";
    }

    @PostMapping("/{boardId}/edit")
    public String edit(BoardCreateForm boardForm, @PathVariable Long boardId) {

        boardService.updateBoard(boardId, boardForm);

        return "redirect:/board";
    }

    @GetMapping("/{boardId}/delete")
    public String delete(@PathVariable long boardId) {

        boardService.deleteBoard(boardId);
        return "redirect:/board"; // 삭제 후 목록 페이지로 리다이렉트
    }


    //    @PostMapping("/{boardId}/edit/likeCount")
//    public String editLikeCount(BoardForm boardForm, @PathVariable Long boardId) {
//        // 이걸하려면 멤버마다 그 보드에 대한 like를 유지하고 있는지에 대한 db 컬럼이 필요하다.
//        return "redirect:/board";
//    }



    //    //@GetMapping()
//    public String boards(@ModelAttribute("boardSearch") BoardSearchCond boardSearch, Model model) {
//        List<Board> boards = boardService.findBoards();
//        List<BoardForm> boardForms = boardService.boardsToBoardForms(boards);
//        model.addAttribute("boards", boardForms);
//        return "board/boards";
//    }
//
//    //    @GetMapping("/search")
//    public String searchBoards(@ModelAttribute("boardSearch") BoardSearchCond boardSearch, @RequestParam(required = false) String sort, Model model) {
//        List<Board> boards = boardService.findSearchedAndSortedBoards(boardSearch);
//
//        List<BoardForm> boardForms = boardService.boardsToBoardForms(boards);
//        model.addAttribute("boards", boardForms);
//        model.addAttribute("boardSearch", boardSearch);
//
//        return "board/boards";
//    }
}
