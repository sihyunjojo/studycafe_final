package project.studycafe.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;
    private Integer likeCount;

    @OneToMany(mappedBy = "comment")
    private List<Reply> replies;


    @PrePersist
    public void prePersist() {
        if (this.likeCount == null) {
            this.likeCount = 0;
        }
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", board=" + board.getId() +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}
