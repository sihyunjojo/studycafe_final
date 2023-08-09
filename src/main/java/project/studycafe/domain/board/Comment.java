package project.studycafe.domain.board;

import lombok.*;
import project.studycafe.domain.base.BaseTimeEntity;
import project.studycafe.domain.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // cascade 넣었다가.... 이거 삭제되니 관련된 member 다 삭제.. -> board 다 삭제..
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // cascade 넣었다가.... 이거 삭제되니 관련된 board 다 삭제..
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();


    public static Comment createComment(Member member, Board board, String content) {
        Comment newComment = new Comment();
        newComment.setBoard(board);
        newComment.setMember(member);
        newComment.setContent(content);

        return newComment;
    }

    public void updateComment(String content) {
        this.content = content;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.getId());
        map.put("memberId", member);
        map.put("boardId", board);
        map.put("content", content);
        map.put("replies", replies);
        return map;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", board=" + board.getId() +
                ", content='" + content + '\'' +
                '}';
    }

    public Long getId(){
        if (id == null) {
            throw new RuntimeException();
        }
        long newId = id;
        return newId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setBoard(Board board) {
        this.board = board;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

}
