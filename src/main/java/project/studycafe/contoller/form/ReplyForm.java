package project.studycafe.contoller.form;

import lombok.Data;

@Data
public class ReplyForm {
    private Long boardId;
    private Long commentId;
    private Long memberId;
    private String content;
}
