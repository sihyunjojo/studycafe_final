package project.studycafe.app.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;


import java.util.Optional;

public interface JpaFileRepository extends JpaRepository<AttachmentFile, Long> {
    Optional<AttachmentFile> findFirstByBoardAndAttachmentFileName(Board board, String attachmentFileName);
    Optional<AttachmentFile> findByUniqueFileName(String uniqueFileName);

    void deleteByBoardId(long BoardId);
    void deleteByUniqueFileName(String UniqueFileName);
}
