package project.studycafe.domain.board;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import project.studycafe.domain.base.BaseTimeEntity;
import project.studycafe.domain.base.Statistics;
import project.studycafe.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Entity
@Getter @Setter
@NoArgsConstructor
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // member 를 어떤 이름으로 order 테이블의 컬럼명으로 저장하는지
    private Member member;

    @NotEmpty
    private String title;
    private String category; //추후 추상클래스로 만들어서 관리해야할지도
    private String content;
    private String popup; // 추후에 객체 따로만들어야할지도

    @Embedded
    private Statistics statistics;

    //Cascadetype.all을 하게되면 세션에 2개의 같은 pk를 가진 attach엔티티가 발생하여서 에러가 발생한다.
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<AttachmentFile> attachmentFiles = new ArrayList<>(); // 추후에 객체 따로만들어야할지도

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void removeAttachmentFile(AttachmentFile attachmentFile) {
        this.attachmentFiles.remove(attachmentFile);
    }

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
        log.info("this ={}", this);
        log.info("member.getBoards() = {}", this.member.getBoards());
    }


    //==비즈니스코드==//
    public Map<String, Object> toMap() {
        Map<String, Object> statisticsMap = statistics.toMap();
        Map<String, Object> boardMap = new HashMap<>();
        boardMap.put("id", id);
        boardMap.put("member", member);
        boardMap.put("title", title);
        boardMap.put("category", category);
        boardMap.put("content", content);
        boardMap.put("popup", popup);
        boardMap.put("readCount", statisticsMap.get("readCount"));
        boardMap.put("likeCount", statisticsMap.get("likeCount"));
        boardMap.put("attachmentFiles", attachmentFiles);
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
                .append(", title='").append(title).append('\'')
                .append(", category='").append(category).append('\'')
                .append(", content='").append(content).append('\'')
                .append(", popup='").append(popup).append('\'')
                .append(", statistics=").append(statistics.toString())
                .append(", attachmentFiles=[");

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
}
