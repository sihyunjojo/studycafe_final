package project.studycafe.contoller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentForm {

    @NotNull
    private long boardId;
    @NotNull
    private long memberId;
    @NotNull
    private String content;
}
