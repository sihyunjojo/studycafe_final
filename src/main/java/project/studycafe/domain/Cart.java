package project.studycafe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public String getMember() {
        return member.getName();
    }
}
