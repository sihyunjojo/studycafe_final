package project.studycafe.domain.form.board;

import lombok.Data;

@Data
public class ReplyForm {
    private Long boardId;
    private Long commentId;
    private Long memberId;
    private String content;
}
