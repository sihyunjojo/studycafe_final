package project.studycafe.domain.board;

import lombok.*;
import project.studycafe.domain.base.BaseTimeEntity;
import project.studycafe.domain.base.Statistics;
import project.studycafe.domain.board.Info.BoardAddInfo;
import project.studycafe.domain.board.Info.BoardBaseInfo;
import project.studycafe.domain.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // member 를 어떤 이름으로 order 테이블의 컬럼명으로 저장하는지
    private Member member;

    @Embedded
    private BoardBaseInfo boardBaseInfo;
    @Embedded
    private BoardAddInfo boardAddInfo;
    @Embedded
    private Statistics statistics;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public static Board createBoard(Member member, BoardBaseInfo boardBaseInfo) {
        Board board = new Board();

        board.setMember(member);
        board.setBoardBaseInfo(boardBaseInfo);
        board.setStatistics(new Statistics(0, 0));

        return board;
    }

    public void updateBoardBaseInfo(BoardBaseInfo boardBaseInfo) {
        this.boardBaseInfo = boardBaseInfo;
    }

    public void removeAttachmentFile(AttachmentFile attachmentFile) {
        this.boardAddInfo.removeAttachmentFile(attachmentFile);
    }

    public void addAttachmentFile(AttachmentFile attachmentFile) {
        this.boardAddInfo.addAttachmentFile(attachmentFile);
    }

    //==연관관계 메서드==//
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    //==비즈니스코드==//
    public Map<String, Object> toMap() {
        Map<String, Object> boardMap = new HashMap<>();
        boardMap.put("id", id);
        boardMap.put("member", member);
        boardMap.put("title", boardBaseInfo.toMap().get("title"));
        boardMap.put("category", boardBaseInfo.toMap().get("category"));
        boardMap.put("content", boardBaseInfo.toMap().get("content"));
        boardMap.put("createTime", super.getCreatedTime());
        boardMap.put("updateTime", super.getUpdatedTime());
        boardMap.put("readCount", statistics.toMap().get("readCount"));
        boardMap.put("likeCount", statistics.toMap().get("likeCount"));
        boardMap.put("attachmentFiles", boardAddInfo.toMap().get("attachmentFiles"));
        boardMap.put("comments", comments);
        return boardMap;
    }


    public void upLikeCount() {
        this.statistics.upLikeCount();
    }

    public void downLikeCount() {
        this.statistics.downLikeCount();
    }

    public void upReadCount() {
        this.statistics.upReadCount();
    }

    public void downReadCount() {
        this.statistics.downReadCount();
    }


    //==편의 메서드==//
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Board{")
                .append("id=").append(id)
                .append(", member=").append(member.getName())
                .append(", title='").append(boardBaseInfo.toMap().get("title")).append('\'')
                .append(", category='").append(boardBaseInfo.toMap().get("category")).append('\'')
                .append(", content='").append(boardBaseInfo.toMap().get("content")).append('\'')
                .append(", statistics=").append(statistics.toString())
                .append(", attachmentFiles=[");

        if ((List<AttachmentFile>) boardAddInfo.toMap().get("attachmentFiles") != null) {
            List<AttachmentFile> attachmentFiles = (List<AttachmentFile>) boardAddInfo.toMap().get("attachmentFiles");
            for (AttachmentFile attachmentFile : attachmentFiles) {
                sb.append(attachmentFile.toString()).append(", ");
            }
            // 마지막 쉼표 제거
            if (!attachmentFiles.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
        }
        sb.append("]}");

        return sb.toString();
    }

    // 진짜 조회만 하는 getId()
    public Long getId() {
        Long newId = id;
        return newId;
    }

    public List<AttachmentFile> getAttachmentFiles() {
        return boardAddInfo.getAttachmentFiles();
    }

    //==연관관계 메서드==//
    private void setMember(Member member) {
        this.member = member;
        member.addBoard(this);
    }

    private void setBoardBaseInfo(BoardBaseInfo boardBaseInfo) {
        this.boardBaseInfo = boardBaseInfo;
    }

    private void setBoardAddInfo(BoardAddInfo boardAddInfo) {
        this.boardAddInfo = boardAddInfo;
    }

    private void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
