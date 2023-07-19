package project.studycafe.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // cascade 넣었다가.... 이거 삭제되니 관련된 mmeber 다 삭제.. -> board 다 삭제..
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // cascade 넣었다가.... 이거 삭제되니 관련된 board 다 삭제..
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;
    private Integer likeCount;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();


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
