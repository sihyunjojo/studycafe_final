package project.studycafe.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class CartProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Boolean purchasedCheck;
    private Integer count;
    private Integer totalPrice;

    @PrePersist
    public void setting(){
        if (this.purchasedCheck == null) {
            this.purchasedCheck = false;
        }
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        cart.getCartProductList().add(this);

    }

    @Override
    public String toString() {
        return "CartProduct{" +
                "id=" + id +
                ", cart=" + cart.getId() +
                ", product=" + product.getName() +
                ", purchasedCheck=" + purchasedCheck +
                ", count=" + count +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

