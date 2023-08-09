package project.studycafe.domain.board;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }


    //==비즈니스코드==//
    public Map<String, Object> toMap() {
        Map<String, Object> statisticsMap = statistics.toMap();

        Map<String, Object> boardMap = new HashMap<>();
        boardMap.put("id", id);
        boardMap.put("member", member);
        boardMap.put("title", boardBaseInfo.toMap().get("title"));
        boardMap.put("category", boardBaseInfo.toMap().get("category"));
        boardMap.put("content", boardBaseInfo.toMap().get("content"));
        boardMap.put("readCount", statisticsMap.get("readCount"));
        boardMap.put("likeCount", statisticsMap.get("likeCount"));
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

        List<AttachmentFile> attachmentFiles = (List<AttachmentFile>) boardAddInfo.toMap().get("attachmentFiles");
        for (AttachmentFile attachmentFile : attachmentFiles) {
            sb.append(attachmentFile.toString()).append(", ");
        }

        // 마지막 쉼표 제거
        if (!attachmentFiles.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]}");

        return sb.toString();
    }
    // 진짜 조회만 하는 getId()
    public long getId(){
        if (id == null) {
            throw new RuntimeException();
        }
        long nowId = id;
        return nowId;
    }

    public List<AttachmentFile> getAttachmentFiles() {
        return boardAddInfo.getAttachmentFiles();
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

    private void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
