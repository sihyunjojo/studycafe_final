package project.studycafe.app.controller.form.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardForm {

    private Long id;
    @NotNull
    private Long memberId;
    private String memberNickname;
    private String memberName;
    @NotBlank
    private String title;
    @NotBlank
    private String category;
    private String content;
    private LocalDateTime createdTime;

    private List<AttachmentFileForm> attachmentFiles;
    private List<CommentForm> comments;

    private Integer readCount;
    private Integer likeCount;

    public BoardForm(Long memberId, String title, String category, String content) {
        this.memberId = memberId;
        this.title = title;
        this.category = category;
        this.content = content;
    }
}
