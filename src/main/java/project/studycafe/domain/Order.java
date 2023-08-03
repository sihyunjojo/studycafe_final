package project.studycafe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static project.studycafe.domain.OrderStatus.WAIT;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //연관관계의 주인은 mapped by 를 사용하지 않는다.
    //연관관계의 주인은 외래키가 있는 쪽이 주인이 된다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // member 를 어떤 이름으로 order 테이블의 컬럼명으로 저장하는지
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Integer totalPrice = 0;

    //1:1 관계에서 외래키는 주테이블에 존재하는 것으로 하자. 그게 제일 편하다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "delivery_Id") // delivery 를 어떤 이름으로 order 테이블의 컬럼명으로 저장하는지
    private Delivery delivery;

    // 추후 결재 할시 결재 정보

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @PrePersist
    void setting() {
        if (this.status == null) {
            this.status = WAIT;
        }
        if (this.delivery == null) {
            this.delivery = new Delivery(member, new Address(), DeliveryStatus.READY);
        }
        if (this.totalPrice == 0) {
            for (OrderItem orderItem : orderItems) {
                log.info("+ Price ={} , {} , {}", orderItem.getAllPrice(),orderItem, orderItems);
                this.totalPrice += orderItem.getAllPrice();
            }
        }
    }

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        log.info("orderItem = {}", orderItem);
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        log.info("orderItem4 = {}",this.totalPrice);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public void removeOrderItems(OrderItem orderItem) {
        orderItem.setOrder(null); // Set the order item's order reference to null
        this.orderItems.remove(orderItem);
    }


    public static Order createOrder(Member member, OrderItem... orderItems) {
        Order order = new Order();
        order.setDelivery(new Delivery(member, new Address(), DeliveryStatus.READY));
        order.setMember(member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        log.info("orderItem1 = {}",orderItems);
        log.info("orderItem2 = {}",order.totalPrice);

        return order;
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        log.info("orderItem2 = {}",orderItems);
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            order.totalPrice += orderItem.getAllPrice();
        }
        return order;
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    //==비즈니스 로직==//

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order{");
        sb.append("id=").append(id);
        sb.append(", member=").append(member.getId());
        sb.append(", orderItems=[");

        // Loop through the orderItems list and append their representations
        for (int i = 0; i < orderItems.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(orderItems.get(i).toString());
        }

        sb.append("]");
        sb.append(", orderTotalPrice=").append(totalPrice);
        sb.append(", delivery=").append(delivery.toString());
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }


    //    public void setOrderItem(OrderItem orderItem) {
//        log.info("orderItem = {}", orderItem);
//        this.orderItems = new ArrayList<>();
//        addOrderItem(orderItem);
//    }

//    public void setOrderItems(List<OrderItem> orderItems) {
////        this.orderItems = orderItems;
//        for (OrderItem orderItem : orderItems) {
//            orderItem.setOrder(this);
//        }
//    }

}
