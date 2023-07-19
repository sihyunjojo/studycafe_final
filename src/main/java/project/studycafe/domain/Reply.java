package project.studycafe.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Reply extends BaseTimeEntity{
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
    private Integer likeCount;

    @PrePersist
    public void prePersist() {
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", comment=" + comment.getId() +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}
