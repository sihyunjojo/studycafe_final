package project.studycafe.app.controller.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.app.service.dto.PageMaker;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.controller.form.board.BoardCreateForm;
import project.studycafe.app.controller.form.board.BoardForm;
import project.studycafe.app.controller.form.board.BoardUpdateForm;
import project.studycafe.app.domain.member.Member;
import project.studycafe.helper.resolver.argumentresolver.Login;
import project.studycafe.app.service.dto.searchDto.BoardSearchCond;
import project.studycafe.app.service.FileService;
import project.studycafe.app.service.board.BoardService;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;


    private static final int BASIC_PER_PAGE_NUM = 10;    // 페이지당 보여줄 게시판 개수

    @GetMapping()
    public String boards(@ModelAttribute("boardSearch") BoardSearchCond boardSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {

//        String[] beanNames = applicationContext.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }

        List<Board> boardsWithoutNotice = boardService.getBoardsByCategoryNotOrderByCreatedTimeDesc("공지사항");
        List<Board> notices = boardService.getBoardsByCategoryByCreatedTimeDesc("공지사항");
        boardSearch.setPerPageNum(BASIC_PER_PAGE_NUM);

        List<Board> boardList = boardService.getBoardList(page, BASIC_PER_PAGE_NUM, boardsWithoutNotice);
        boardList.addAll(0, notices);

        PageMaker pageMaker = PageMaker.builder()
                .totalBoardCount(boardsWithoutNotice.size())
                .currentPage(page)
                .perPageNum(BASIC_PER_PAGE_NUM)
                .build();


        // 클라이언트 처리
        List<BoardForm> boardForms = boardService.boardsToBoardForms(boardList);
        model.addAttribute("boards", boardForms);
        model.addAttribute("pageMaker", pageMaker);

        return "board/boards";
    }

    @GetMapping("/search")
    public String searchBoards(@ModelAttribute("boardSearch") BoardSearchCond boardSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Board> findSortedBoards = boardService.getSearchedAndSortedBoards(boardSearch);
        List<Board> notices = boardService.getBoardsByCategoryByCreatedTimeDesc("공지사항");

        List<Board> findBoardList = boardService.getBoardList(page, boardSearch.getPerPageNum(), findSortedBoards);

        log.info("findboard = {}, notices = {}", findSortedBoards.size(), notices.size());
        PageMaker pageMaker = PageMaker.builder()
                .totalBoardCount(findSortedBoards.size())
                .currentPage(page)
                .perPageNum(boardSearch.getPerPageNum())
                .build();

        log.info("{}", pageMaker);

        // 공지사항을 검색했을 시에 2번 안 띄우게
        if (!boardSearch.getCategory().equals("공지사항")) {
            findBoardList.addAll(0, notices);
        }


        if (boardSearch.getSort() != null) {
            switch (boardSearch.getSort()) {
                case "boardReadCountUp":
                    boardSearch.setSort("boardReadCountDown");
                    break;
                case "boardReadCountDown":
                    boardSearch.setSort("boardReadCountUp");
                    break;
                case "boardLikeCountUp":
                    boardSearch.setSort("boardLikeCountDown");
                    break;
                case "boardLikeCountDown":
                    boardSearch.setSort("boardLikeCountUp");
                    break;
            }
        }

        // 클라이언트 처리
        List<BoardForm> boardForms = boardService.boardsToBoardForms(findBoardList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("boards", boardForms);
        model.addAttribute("boardSearch", boardSearch);

        return "board/boards";
    }

    @GetMapping("/{boardId}")
    public String board(@PathVariable long boardId, Model model) {
//        Board board = boardService.findById(boardId).orElseThrow();
        Board board = boardService.getBoardWithMemberCommentReplyAttachmentFile(boardId);

        boardService.increaseReadCount(boardId);
        BoardForm boardForm = boardService.boardToBoardForm(board);

        model.addAttribute("board", boardForm);
        return "board/board";
    }

    @GetMapping("/add")
    public String addForm(@Login Member loginMember, Model model) {
        model.addAttribute("board", BoardCreateForm.createEmptyBoardCreateForm());
        model.addAttribute("loginMember", loginMember);
        return "board/addBoardForm";
    }

    @PostMapping("/add")
    public String add(BoardCreateForm form) throws IOException, NoSuchAlgorithmException {
        Long boardId = boardService.addBoard(form);

        if (form.getAttachmentFiles() != null) {
            fileService.storeFiles(form.getAttachmentFiles(), boardId);
//            List<AttachmentFile> storeFiles = fileService.storeFiles(form.getAttachmentFiles(), boardId);
        }

        return "redirect:/board";
    }

    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId).orElseThrow();
        // 여기서 form을 조금 더 잘 만져서 보내면 수정할때, 파일의 형태로 보내서 수정할때, 기존꺼 엎는 느낌으로 갈 수 있을듯.

        BoardForm boardForm = boardService.boardToBoardForm(board);

        log.info("file = {}", boardForm.getAttachmentFiles().toString());
        model.addAttribute("board", boardForm);
        return "board/editBoardForm";
    }

    @PostMapping("/{boardId}/edit")
    public String edit(BoardUpdateForm form, @PathVariable Long boardId) throws IOException, NoSuchAlgorithmException {
        if (!form.getNewAttachmentFiles().isEmpty()) {
            fileService.storeFiles(form.getNewAttachmentFiles(), boardId);
//            List<AttachmentFile> storeFiles = fileService.storeFiles(form.getNewAttachmentFiles(), boardId);
        }
        boardService.updateBoard(boardId, form);

        return "redirect:/board/" + boardId;
    }

    @GetMapping("/{boardId}/delete")
    public String delete(@PathVariable long boardId) {
        Board board = boardService.findById(boardId).orElseThrow();
        List<AttachmentFile> attachmentFiles = board.getAttachmentFiles();

        for (AttachmentFile attachmentFile : attachmentFiles) {
            fileService.deleteFileFromStorage(attachmentFile);
        }

        boardService.deleteBoard(boardId);
        return "redirect:/board";
    }


    @GetMapping("/{boardId}/likeCountUp")
    public String upLikeCountEdit(@PathVariable Long boardId) {
        boardService.upLikeCountBoard(boardId);
        return "redirect:/board/"+ boardId;
    }

    @GetMapping("/{boardId}/likeCountDown")
    public String downLikeCountEdit(@PathVariable Long boardId) {
        boardService.downLikeCountBoard(boardId);
        return "redirect:/board/"+ boardId;
    }
}
