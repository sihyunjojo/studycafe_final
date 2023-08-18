package project.studycafe.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.CartProduct;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartProduct> cartProductList;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart{");
        sb.append("id=").append(id);
        sb.append("member = ").append(member);
        sb.append(", cartProductList=[");


        for (int i = 0; i < cartProductList.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cartProductList.get(i).toString());
        }
        sb.append("]}");
        return sb.toString();
    }
}

