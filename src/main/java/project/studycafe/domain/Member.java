package project.studycafe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Member extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userLoginId;
    private String userPassword;
    private String name;
    private String phone;
    //Embedded type 은 사용자가 직접 정의한 값 타입이다.
    //여기서 Embedded type 을 사용하지 않으면,주소에 관한 정보를 전부 직접 정의해 줘야 되는데
    //그러면 객체지향적이지 않고 응집력을 떨어뜨리는 원인이 된다.
    @Embedded
    private Address address;

    @Email
    private String email;
    private String gender;
    private String birth;
    private String provider;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private MemberLevel memberLevel;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reply> replies = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder //생성을 Builder 패턴으로 하기 위해서
    public Member(Long id, String name, String email, String provider, String nickname) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.nickname = nickname;
    }

    //==연관관계 메서드==//
    public void setCart(Cart cart) {
        this.cart = cart;
        cart.setMember(this);
    }
}