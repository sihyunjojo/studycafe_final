package project.studycafe.app.controller.form.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BoardUpdateForm {
    @NotBlank
    private String title;
    @NotBlank
    private String category;
    @NotNull
    private String content;

    private List<MultipartFile> newAttachmentFiles;

    public BoardUpdateForm(String title, String category, String content) {
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
