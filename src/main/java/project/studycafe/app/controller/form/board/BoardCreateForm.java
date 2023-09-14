package project.studycafe.app.controller.form.board;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardCreateForm {
    @NotNull
    private Long memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String category;
    @NotNull
    private String content;
    private List<MultipartFile> attachmentFiles;

    public BoardCreateForm(Long memberId, String title, String category, String content) {
        this.memberId = memberId;
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
