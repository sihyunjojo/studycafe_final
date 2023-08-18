package project.studycafe.app.controller.form.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardCreateForm {
    private Long memberId;
    private String title;
    private String category;
    private String content;
    private List<MultipartFile> attachmentFiles;
}
