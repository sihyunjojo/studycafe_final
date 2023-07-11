package project.studycafe.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import project.studycafe.domain.AttachmentFile;
import project.studycafe.domain.Board;
import project.studycafe.service.FileService;
import project.studycafe.service.board.BoardService;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping("download/{boardId}/{attachmentFileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long boardId, @PathVariable String attachmentFileName) throws MalformedURLException {
        // 다운받을 수 있는 권한이 있는 사람만 되게 걸러주는 코드

        Board board = boardService.findById(boardId).orElseThrow();
        AttachmentFile findFile = fileService.findFirstByBoardAndAttachmentFileName(board, attachmentFileName).orElseThrow();

        UrlResource resource = new UrlResource("file:" + fileService.getFullPath(attachmentFileName)); // 파일의 전체 경로
        log.info("resource = {}", resource);
        log.info("uploadFileName = {} ", attachmentFileName);

        //다운로드 시키는 규약임
        //content-disposition을 attachment로 받으면서 첨부파일로 인식해서 다운로드 받음
        String encodedUploadFileName = UriUtils.encode(attachmentFileName, StandardCharsets.UTF_8); //한글이나 특수문자 오류방지(웹브라우저마다 다를 수도 있는데 그건 알아서 찾아서)
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\""; //다운로드 받을 파일

        log.info("encodedUploadFileName = {}", encodedUploadFileName);
        log.info("contentDisposition = {}", contentDisposition);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
