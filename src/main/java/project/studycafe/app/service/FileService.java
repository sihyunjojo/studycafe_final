package project.studycafe.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.repository.board.board.JpaBoardRepository;
import project.studycafe.app.repository.file.JpaFileRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final JpaFileRepository fileRepository;
    private final JpaBoardRepository boardRepository;

    // 파일 등록하는 순간 파일이 여기로 복사되서 들어감.(저장소의 위치)
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // 이미지 여러개 날라올때
    public void storeFiles(List<MultipartFile> multipartFiles, Long boardId){
//        List<AttachmentFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                AttachmentFile uploadFile = storeFile(multipartFile, boardId);
//                storeFileResult.add(uploadFile);
            }
        }
//        return storeFileResult;
    }

    // 이미지 하나 날라올때
    public AttachmentFile storeFile(MultipartFile multipartFile, Long boardId){
        log.info("store file start");
        if (multipartFile.isEmpty()) {
            return null;
        }

        //image.png
        String originalFilename = multipartFile.getOriginalFilename();
        //서버에 저장하는 파일명 uuid+.+확장자
        String storeFileName = createStoreFileName(originalFilename);
        long fileSize = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
        log.info("file contentType = {}", contentType);

        AttachmentFile attachmentFile = new AttachmentFile(storeFileName, originalFilename, fileSize);
        attachmentFile.setBoard(boardRepository.findById(boardId).orElseThrow());

        log.info("originalFilename = {}", originalFilename);
        log.info("storeFileName = {}", storeFileName);


        //파일정보 DB에 저장
        //setBoard()시 내부에서 attachmentFile 을 만들면서 자동으로 저장해줌.
//        fileRepository.save(attachmentFile);

        //파일 저장장치에 저장
        try {
            multipartFile.transferTo(new File(getFullPath(storeFileName))); //transferTo() 업로드된 파일을 지정된 경로로 전송(이동)하는 기능을 제공한다.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return attachmentFile;
    }


    public void deleteFile(AttachmentFile findFile) {
        log.info("findFile = {}", findFile);
        List<Board> boardContainedAttachmentFile = boardRepository.findALlByBoardAddInfo_AttachmentFiles(findFile);
        log.info("boardContainedAttachmentFile ={}", boardContainedAttachmentFile);
        for (Board board : boardContainedAttachmentFile) {
            //보드에서 연관된 파일 없애기
            board.removeAttachmentFile(findFile);
        }

        //파일테이블에서 없애기
        fileRepository.deleteByUniqueFileName(findFile.getUniqueFileName());

        //파일 저장장치에서 삭제
        deleteFileFromStorage(findFile);

    }

    public void deleteFileFromStorage(AttachmentFile findFile) {
        File storeFile = new File(getFullPath(findFile.getUniqueFileName()));
        if (storeFile.exists()) {
            if (storeFile.delete()) {
                log.info("저장장치에서 파일 삭제");
            } else {
                // 파일 삭제 실패
            }
        } else {
            // 파일이 존재하지 않음
        }
    }


    public Optional<AttachmentFile> findFirstByBoardAndAttachmentFileName(Board board, String attachmentFileName) {
        return fileRepository.findFirstByBoardAndAttachmentFileName(board, attachmentFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
