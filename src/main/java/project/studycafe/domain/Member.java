package project.studycafe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

import static project.studycafe.domain.MemberLevel.USER;


@Entity
//@Table(uniqueConstraints = {
//		@UniqueConstraint(name = "order_history_id_unique", columnNames = {"order_history_id"})
//})
@Getter @Setter
@NoArgsConstructor
public class Member extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userLoginId;
    private String userPassword;
    private String name;
    @Column(unique = true)
    private String nickname;
    private String phone;
    @Email
    private String email;
    private String gender;
    private String birth;

    //Embedded type 은 사용자가 직접 정의한 값 타입이다.
    //여기서 Embedded type 을 사용하지 않으면,주소에 관한 정보를 전부 직접 정의해 줘야 되는데
    //그러면 객체지향적이지 않고 응집력을 떨어뜨리는 원인이 된다.
    @Embedded
    private Address address;

    private String provider;

    @Enumerated(EnumType.STRING)
    private MemberLevel memberLevel;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Cart cart;

    @PrePersist
    public void setting() {
        if (this.memberLevel == null) {
            this.memberLevel = USER;
        }
        if (this.address == null) {
            this.address = new Address(null,null,null);
        }
    }

    @Builder //생성을 Builder 패턴으로 하기 위해서
    public Member(Long id, String name, String email, String provider, String nickname) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.nickname = nickname;
        this.address = new Address("","","");
    }

    //==연관관계 메서드==//
    public void setCart(Cart cart) {
        this.cart = cart;
        cart.setMember(this);
    }


    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", userLoginId='" + userLoginId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + birth + '\'' +
                ", provider='" + provider + '\'' +
                ", nickname='" + nickname + '\'' +
                ", memberLevel=" + memberLevel +
                ", createdTime = " + getCreatedTime() +
                '}';
    }
}
