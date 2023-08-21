package project.studycafe.app.domain.board;

import lombok.*;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.base.Statistics;
import project.studycafe.app.domain.board.Info.BoardAddInfo;
import project.studycafe.app.domain.board.Info.BoardBaseInfo;
import project.studycafe.app.domain.member.Member;

import javax.persistence.*;
import java.util.*;

import static project.studycafe.app.domain.base.Statistics.createStatistics;

@Entity
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private BoardBaseInfo boardBaseInfo;
    @Embedded
    private BoardAddInfo boardAddInfo;
    @Embedded
    private Statistics statistics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // member 를 어떤 이름으로 order 테이블의 컬럼명으로 저장하는지
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public static Board createBoard(Member member, BoardBaseInfo boardBaseInfo) {
        Board board = new Board();

        board.setMember(member);
        board.setBoardBaseInfo(boardBaseInfo);
        board.setBoardAddInfo(BoardAddInfo.createBoardAddInfo(new ArrayList<>()));
        board.setComments(new HashSet<>());
        board.setStatistics(createStatistics(0, 0));
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
    public void addAttachmentFiles(List<AttachmentFile> attachmentFiles) {
        attachmentFiles.stream().forEach(a -> boardAddInfo.addAttachmentFile(a));
    }

    //==연관관계 메서드==//
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    //==비즈니스코드==//
    public Map<String, Object> toMap() {
        Map<String, Object> boardMap = new HashMap<>();
        boardMap.put("id", id);
        boardMap.put("memberName", member.toMap().get("name"));
        boardMap.put("memberNickname", member.toMap().get("nickname"));
        boardMap.put("title", boardBaseInfo.toMap().get("title"));
        boardMap.put("category", boardBaseInfo.toMap().get("category"));
        boardMap.put("content", boardBaseInfo.toMap().get("content"));
        boardMap.put("createTime", super.getCreatedTime());
        boardMap.put("updateTime", super.getUpdatedTime());
        boardMap.put("readCount", statistics.toMap().get("readCount"));
        boardMap.put("likeCount", statistics.toMap().get("likeCount"));
        boardMap.put("attachmentFiles", (List<AttachmentFile>) boardAddInfo.toMap().get("attachmentFiles"));
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
}
