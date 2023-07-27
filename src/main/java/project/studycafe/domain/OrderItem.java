package project.studycafe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class OrderItem extends BaseTimeEntity{

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

    public OrderItem(Product product, int count) {
        this.product = product;
        this.count = count;
        this.allPrice = count * product.getPrice();

        product.removeStock(count);
    }

    // 이게 도대체 뭔 코드임???
    // 삭제했다가 그냥 다시 넣어주는 코드인데?
    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.getOrderItems().remove(this);
        }
        this.order = order;
        order.getOrderItems().add(this);
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
