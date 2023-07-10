package project.studycafe.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
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



    public AttachmentFile(String attachmentFileName, String uniqueFileName) {
        this.attachmentFileName = attachmentFileName;
        this.uniqueFileName = uniqueFileName;
    }
}
