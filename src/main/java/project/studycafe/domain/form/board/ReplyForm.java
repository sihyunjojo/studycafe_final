package project.studycafe.domain.form.board;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import project.studycafe.domain.board.Comment;
import project.studycafe.domain.board.Reply;
import project.studycafe.domain.member.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class ReplyForm {
    private Long id;
    private Long boardId;
    private Long commentId;

    private Long memberId;
    private String memberName;

    private String content;

    public static List<ReplyForm> createReplyForms(List<Reply> replys) {
        List<ReplyForm> replyForms = new ArrayList<>();

        for (Reply reply : replys) {
            Map<String, Object> replyMap = reply.toMap();
            ReplyForm replyForm = new ReplyForm();
            log.info("map ={}", replyMap);

            replyForm.setId((Long) replyMap.get("id"));
            replyForm.setBoardId(((Comment) replyMap.get("comment")).getId());
            replyForm.setMemberId(( (Member) replyMap.get("member")).getId());
            replyForm.setMemberName(( (Member) replyMap.get("member")).getName());
            replyForm.setContent((String) replyMap.get("content"));
            replyForms.add(replyForm);
        }

        return replyForms;
    }
}
