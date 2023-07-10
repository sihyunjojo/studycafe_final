package project.studycafe.contoller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.studycafe.domain.AttachmentFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardForm {

    private long id;
    private String memberNickname;
    private String memberName;
    private String title;
    private String category;
    private String content;
    private LocalDateTime createTime;
    private List<AttachmentFile> attachmentFiles; // 추후에 객체 따로만들어야할지도
    private String popup;
    private long readCount;
    private long likeCount;

}
