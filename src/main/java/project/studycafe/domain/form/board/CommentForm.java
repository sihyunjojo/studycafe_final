package project.studycafe.domain.form.board;

import lombok.Data;
import project.studycafe.domain.board.Board;
import project.studycafe.domain.board.Comment;
import project.studycafe.domain.enums.FileType;
import project.studycafe.domain.member.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class CommentForm {

    private Long Id;
    private Long boardId;

    private Long memberId;
    private String memberName;

    private List<ReplyForm> replies;

    private String content;

    public static List<CommentForm> createCommentForms(List<Comment> comments) {
        List<CommentForm> commentForms = new ArrayList<>();

        for (Comment comment : comments) {
            Map<String, Object> commentMap = comment.toMap();
            CommentForm commentForm = new CommentForm();

            commentForm.setId((Long) comment.toMap().get("id"));
            commentForm.setBoardId(((Board) comment.toMap().get("board")).getId());
            commentForm.setMemberId(( (Member) comment.toMap().get("member")).getId());
            commentForm.setMemberName(( (Member) comment.toMap().get("member")).getName());
            commentForm.setContent((String) comment.toMap().get("content"));
            commentForms.add(commentForm);
        }

        return commentForms;
    }
}
