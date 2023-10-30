package project.studycafe.app.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.app.domain.Order;
import project.studycafe.app.domain.base.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int count = 1;
    private int allPrice;

    // 값을 받아오고 실행.
    @PrePersist
    void setting() {
        if (this.count != 1) {
            this.allPrice = count * product.getPrice();
        }
    }

    //==생성 메서드==//
    public static OrderItem createOrderItem(Product product, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setCount(count);
        orderItem.setAllPrice(count * product.getPrice());

        product.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        getProduct().addStock(count);
    }


    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product.getId() +
                ", count=" + count +
                ", allPrice=" + allPrice +
                '}';
    }
}
