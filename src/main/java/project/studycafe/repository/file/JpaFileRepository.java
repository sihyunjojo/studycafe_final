package project.studycafe.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.AttachmentFile;
import project.studycafe.domain.Board;
import project.studycafe.domain.Member;

import java.util.Optional;

public interface JpaFileRepository extends JpaRepository<AttachmentFile, Long> {
    Optional<AttachmentFile> findFirstByBoardAndAttachmentFileName(Board board, String attachmentFileName);
    void deleteByBoardId(long BoardId);
}
