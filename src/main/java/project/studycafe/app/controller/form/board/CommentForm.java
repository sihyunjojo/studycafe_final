package project.studycafe.app.controller.form.board;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.board.Reply;
import project.studycafe.app.domain.member.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class CommentForm {

    private Long Id;
    private Long boardId;

    private Long memberId;
    private String memberName;

    private List<ReplyForm> replies;

    private String content;

    public static List<CommentForm> createCommentForms(List<Comment> comments) {
        if (comments == null) {
            return new ArrayList<>();
        }
        List<CommentForm> commentForms = new ArrayList<>();

        for (Comment comment : comments) {
            Map<String, Object> commentMap = comment.toMap();
            CommentForm commentForm = new CommentForm();

            List<Reply> getReplies = (List<Reply>) commentMap.get("replies");
            List<ReplyForm> replyForms = ReplyForm.createReplyForms(getReplies);

            commentForm.setId((Long) commentMap.get("id"));
            commentForm.setBoardId(((Board) commentMap.get("board")).getId());
            commentForm.setMemberId(( (Member) commentMap.get("member")).getId());
            commentForm.setMemberName(( (Member) commentMap.get("member")).getName());
            commentForm.setContent((String) commentMap.get("content"));
            commentForm.setReplies(replyForms);
            commentForms.add(commentForm);
        }

        return commentForms;
    }
}
