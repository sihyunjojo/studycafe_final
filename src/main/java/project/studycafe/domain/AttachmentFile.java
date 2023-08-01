package project.studycafe.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.reflection.XMember;

import javax.persistence.*;

@Slf4j
@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AttachmentFile extends BaseTimeEntity {
    @Id
    private String uniqueFileName;

    private String attachmentFileName;
    private long attachmentFileSize;

    @Enumerated(EnumType.STRING)
    private FileType attachmentFileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id") // AttachmentFile 테이블의 외래 키 컬럼의 이름을 지정하는 것
    private Board board;

    public AttachmentFile(String uniqueFileName, String attachmentFileName) {
        this.uniqueFileName = uniqueFileName;
        this.attachmentFileName = attachmentFileName;
    }

    public AttachmentFile(String uniqueFileName, String attachmentFileName, long attachmentFileSize) {
        this.uniqueFileName = uniqueFileName;
        this.attachmentFileName = attachmentFileName;
        this.attachmentFileSize = attachmentFileSize;
    }

    //==연관관계 메서드==//
    // 이게 도대체 뭔 코드임???
    // 삭제했다가 그냥 다시 넣어주는 코드인데?
    public void setBoard(Board board) {
        this.board = board;
        board.getAttachmentFiles().add(this);
    }

    @Override
    public String toString() {
        return "AttachmentFile{" +
                "attachmentFileName='" + attachmentFileName + '\'' +
                "uniqueFileName='" + uniqueFileName + '\'' +
                '}';
    }
}
