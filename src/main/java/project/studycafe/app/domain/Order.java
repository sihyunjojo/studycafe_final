package project.studycafe.app.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import project.studycafe.app.domain.enums.status.DeliveryStatus;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.enums.status.OrderStatus;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.OrderItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static project.studycafe.app.domain.Address.createAddress;
import static project.studycafe.app.domain.enums.status.DeliveryStatus.READY;
import static project.studycafe.app.domain.enums.status.OrderStatus.WAIT;


@Slf4j
@Entity
// 타임리프에 객체 통째로 보내서 getter public 으로 해야함..
@Getter @Setter(AccessLevel.PRIVATE)
@Table(name = "orders")
@NamedEntityGraph(name = "Order.withMember", attributeNodes = {
        @NamedAttributeNode("member")})
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
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "delivery_Id") // delivery 를 어떤 이름으로 order 테이블의 컬럼명으로 저장하는지

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    protected Order() {}

    // 추후 결재 할시 결재 정보

    @PrePersist
    void setting() {
        if (this.orderstatus == null) {
            this.orderstatus = WAIT;
        }
        if (this.deliveryStatus == null) {
            this.deliveryStatus = READY;
        }
        if (this.address == null) {
            address = createAddress("", "", "");
        }
        if (this.totalPrice == 0) {
            for (OrderItem orderItem : orderItems) {
                log.info("+ Price ={} , {} , {}", orderItem.getAllPrice(), orderItem, orderItems);
                this.totalPrice += orderItem.getAllPrice();
            }
        }
    }

    //==비즈니스 로직==//
    public static Order createOrder(Member member, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        log.info("create List - > orderItem2 = {}", orderItems);
        Order order = new Order();
        order.setAddress(new Address());
        order.setMember(member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            order.totalPrice += orderItem.getAllPrice();
        }
        return order;
    }

    public static Order createOrder(Member member, Address address, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setAddress(address);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    public void updateOrder(Integer totalPrice, Address address, OrderStatus orderStatus, DeliveryStatus deliveryStatus) {
        setAddress(address);
        setTotalPrice(totalPrice);
        setOrderstatus(orderStatus);
        setDeliveryStatus(deliveryStatus);
    }

    public void updateOrder(Integer totalPrice, Address address) {
        setAddress(address);
        setTotalPrice(totalPrice);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        setOrderstatus(orderStatus);
    }

    public void removeOrderItems(OrderItem orderItem) {
        orderItem.setOrder(null); // Set the order item's order reference to null
        this.orderItems.remove(orderItem);
    }

    /**
     * 주문 취소
     */
    public void cancel() {
        if (deliveryStatus == DeliveryStatus.COMPLETE) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setOrderstatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        log.info("addOrderItem -> orderItem = {}", orderItem);
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        totalPrice += orderItem.getAllPrice();
        log.info("addOrderItem -> orderTotalPrice = {}", totalPrice);
    }

    public Long getId(){
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderstatus;
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
        sb.append(", delivery=").append(address.toString());
        sb.append(", orderStatus=").append(orderstatus);
        sb.append(", deliveryStatus=").append(deliveryStatus);
        sb.append('}');
        return sb.toString();
    }

}
