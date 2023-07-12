package project.studycafe.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter @Setter
@NoArgsConstructor
public class Board extends BaseTimeEntity{
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

    @NotNull
    private Integer readCount;
    @NotNull
    private Integer likeCount;

    @OneToMany(mappedBy = "board")
//    @Column(columnDefinition = "String[]")
    private List<AttachmentFile> attachmentFiles = new ArrayList<>(); // 추후에 객체 따로만들어야할지도

    @OneToMany(mappedBy = "board")
    private List<Comment> Comments;

    //영속상태로 변하기 직전에 시점에 시작됨.
    @PrePersist
    public void setting() {
        if (this.readCount == null && this.likeCount == null) {
            this.readCount = 0;
            this.likeCount = 0;
        }
    }

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
        log.info("this ={}", this);
        log.info("member.getBoards() = {}", this.member.getBoards());
    }

    public void removeAttachmentFile(AttachmentFile attachmentFile) {
        this.attachmentFiles.remove(attachmentFile);
        attachmentFile.setBoard(null);
    }



    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", member=" + member.getName() +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                ", popup='" + popup + '\'' +
                ", readCount=" + readCount +
                ", likeCount=" + likeCount +
//                ", attachmentFiles=" + attachmentFiles.stream().map(a -> a.getAttachmentFileName()) +
//                ", Comments=" + Comments +
                '}';
    }

}
