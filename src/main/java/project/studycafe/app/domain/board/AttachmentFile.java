package project.studycafe.app.domain.board;

import lombok.*;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.enums.FileType;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static AttachmentFile createAttachmentFile(AttachmentFile attachmentFile) {
        AttachmentFile newAttachmentFile = new AttachmentFile();
        newAttachmentFile.setUniqueFileName(attachmentFile.getUniqueFileName());
        newAttachmentFile.setAttachmentFileName(attachmentFile.getAttachmentFileName());
        newAttachmentFile.setAttachmentFileSize(attachmentFile.getAttachmentFileSize());
        newAttachmentFile.setAttachmentFileType(attachmentFile.getAttachmentFileType());
        return newAttachmentFile;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uniqueFileName", uniqueFileName);
        map.put("attachmentFileName", attachmentFileName);
        map.put("attachmentFileSize", attachmentFileSize);
        map.put("attachmentFileType", attachmentFileType);
        return map;
    }


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
    public void setBoard(Board board) {
        this.board = board;
        board.addAttachmentFile(this);
    }

    @Override
    public String toString() {
        return "AttachmentFile{" +
                "attachmentFileName='" + attachmentFileName + '\'' +
                "uniqueFileName='" + uniqueFileName + '\'' +
                '}';
    }


    public String getUniqueFileName() {
        String newUniqueFileName = this.uniqueFileName;
        return newUniqueFileName;
    }

    private String getAttachmentFileName() {
        String newAttachmentFileName = this.attachmentFileName;
        return newAttachmentFileName;    }

    private long getAttachmentFileSize() {
        long newAttachmentFileSize = this.attachmentFileSize;
        return newAttachmentFileSize;
    }

    private FileType getAttachmentFileType() {
        FileType newFileType = this.attachmentFileType;
        return newFileType;
    }

    private void setUniqueFileName(String uniqueFileName) {
        this.uniqueFileName = uniqueFileName;
    }

    private void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    private void setAttachmentFileSize(long attachmentFileSize) {
        this.attachmentFileSize = attachmentFileSize;
    }

    private void setAttachmentFileType(FileType attachmentFileType) {
        this.attachmentFileType = attachmentFileType;
    }
    
}
