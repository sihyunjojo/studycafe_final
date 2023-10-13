package project.studycafe.app.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.CartProduct;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "Cart.withMember", attributeNodes = {
        @NamedAttributeNode("member")})
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartProduct> cartProductList;

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart{");
        sb.append("id= ").append(id);
        sb.append("member = ").append(member.getId());

        if (cartProductList != null) {
            sb.append(", cartProductList=[");

            for (int i = 0; i < cartProductList.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(cartProductList.get(i).toString());
            }
            sb.append("]}");
        }
        return sb.toString();
    }
}

