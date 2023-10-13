package project.studycafe.app.domain.member;

import lombok.*;
import org.springframework.stereotype.Component;
import project.studycafe.app.domain.Address;
import project.studycafe.app.domain.Cart;
import project.studycafe.app.domain.Delivery;
import project.studycafe.app.domain.Order;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.domain.board.Comment;
import project.studycafe.app.domain.board.Reply;
import project.studycafe.app.domain.enums.MemberLevel;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.studycafe.app.domain.Address.createAddress;
import static project.studycafe.app.domain.Cart.createCart;

@Entity
//@Table(uniqueConstraints = {
//		@UniqueConstraint(name = "member_id_unique", columnNames = {"nickname"})
//})
@Getter @Setter
@NoArgsConstructor
@NamedEntityGraph(name = "Member.withCart", attributeNodes = {
        @NamedAttributeNode("cart")})
public class Member extends BaseTimeEntity {
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

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Cart cart;

    @PrePersist
    public void setting() {
        if (this.memberLevel == null) {
            this.memberLevel = MemberLevel.USER;
        }
        if (this.address == null) {
            this.address = createAddress("", "", "");
        }
        if (this.cart == null) {
            cart = createCart(this);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("nickname", nickname);
        return map;
    }

    public void addBoard(Board board) {
        this.boards.add(board);
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
    public void addReply(Reply reply) {
        this.replies.add(reply);
    }

    @Builder //생성을 Builder 패턴으로 하기 위해서
    public Member(Long id, String name, String email, String provider, String nickname) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.nickname = nickname;
        this.cart = createCart(this);
    }

    //==연관관계 메서드==//

    public Long getId(){
        return id;
    }

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
