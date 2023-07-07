package project.studycafe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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
    private String attachmentFile; // 추후에 객체 따로만들어야할지도
    private String popup; // 추후에 객체 따로만들어야할지도

    @NotNull
    private Integer readCount;
    @NotNull
    private Integer likeCount;

    @OneToMany(mappedBy = "board")
    private List<Comment> Comments;

}
