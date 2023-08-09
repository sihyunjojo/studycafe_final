package project.studycafe.domain.board;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.domain.base.BaseTimeEntity;
import project.studycafe.domain.member.Member;

import javax.persistence.*;

@Entity
@NoArgsConstructor
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
    
    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", comment=" + comment.getId() +
                ", content='" + content + '\'' +
                '}';
    }

    public long getId() {
        long newId = this.id;
        return newId;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setComment(Comment comment) {
        this.comment = comment;
    }

    private void setContent(String content) {
        this.content = content;
    }
}
