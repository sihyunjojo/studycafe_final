package project.studycafe.domain.form.board;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentForm {

    private Long boardId;
    private Long memberId;
    private String content;
}
