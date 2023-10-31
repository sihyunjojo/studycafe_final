package project.studycafe.app.domain.board;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.member.Member;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String content;

    public static Reply createReply(Member member, Comment comment, String content) {
        Reply newReply = new Reply();

        newReply.setComment(comment);
        newReply.setMember(member);
        newReply.setContent(content);

        return newReply;
    }

    public void updateReply(String content) {
        this.content = content;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.getId());
        map.put("member", member);
        map.put("comment", comment);
        map.put("content", content);
        return map;
    }


    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", comment=" + comment.getId() +
                ", content='" + content + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId(){
        return member.getId();
    }

    private void setMember(Member member) {
        this.member = member;
        member.addReply(this);
    }

    private void setComment(Comment comment) {
        this.comment = comment;
        comment.addReply(this);
    }

    private void setContent(String content) {
        this.content = content;
    }
}
