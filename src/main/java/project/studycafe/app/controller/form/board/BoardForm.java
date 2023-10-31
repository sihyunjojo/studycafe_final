package project.studycafe.app.controller.form.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data

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


    @Builder(builderMethodName = "easyBuilder", buildMethodName = "buildEasyBoardForm")
    public BoardForm(Long memberId, String title, String category, String content) {
        this.memberId = memberId;
        this.title = title;
        this.category = category;
        this.content = content;
    }

    @Builder
    public BoardForm(Long id, Long memberId, String memberNickname, String memberName, String title, String category,
                     String content, LocalDateTime createdTime, List<AttachmentFileForm> attachmentFiles,
                     List<CommentForm> comments, Integer readCount, Integer likeCount) {
        this.id = id;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.memberName = memberName;
        this.title = title;
        this.category = category;
        this.content = content;
        this.createdTime = createdTime;
        this.attachmentFiles = attachmentFiles;
        this.comments = comments;
        this.readCount = readCount;
        this.likeCount = likeCount;
    }
}
