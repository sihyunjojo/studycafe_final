package project.studycafe.app.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.app.domain.Cart;
import project.studycafe.app.domain.base.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static CartProduct createCartProduct(Cart cart, Product product) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setCount(1);
        cartProduct.setTotalPrice(product.getPrice());

        return cartProduct;
    }

    private void setCart(Cart cart) {
        this.cart = cart;
        cart.getCartProductList().add(this);
    }

    private void setProduct(Product product) {
        this.product = product;
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

