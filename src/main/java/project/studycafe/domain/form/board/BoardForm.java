package project.studycafe.domain.form.board;

import lombok.AllArgsConstructor;
import lombok.Data;

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
    private LocalDateTime createdTime;
    private List<AttachmentFileForm> attachmentFiles; // 추후에 객체 따로만들어야할지도
    private List<CommentForm> comments;
    private long readCount;
    private long likeCount;

}
