package project.studycafe.contoller.form;

import lombok.Data;
import project.studycafe.domain.AttachmentFile;

import java.util.List;

@Data
public class BoardCreateForm {
    private String title;
    private Long memberId;
    private String category;
    private String content;
    private List<AttachmentFile> attachmentFiles;
}
