package project.studycafe.app.controller.form.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
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

    private BoardCreateForm() {
    }

    public static BoardCreateForm createEmptyBoardCreateForm(){
        return new BoardCreateForm();
    }
    @Builder(builderMethodName = "easyBuilder", buildMethodName = "buildEasyBoardCreateForm")
    public BoardCreateForm(Long memberId, String title, String category, String content) {
        this.memberId = memberId;
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
