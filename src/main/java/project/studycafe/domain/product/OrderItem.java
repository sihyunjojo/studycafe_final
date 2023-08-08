package project.studycafe.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.domain.Order;
import project.studycafe.domain.base.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
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