package project.studycafe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.OrderNowForm;
import project.studycafe.domain.*;
import project.studycafe.repository.*;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.repository.product.JpaProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final JpaMemberRepository memberRepository;
    private final JpaProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;

    public long addOrderNow(OrderNowForm form) {
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        OrderItem orderItems = new OrderItem(productRepository.findById(form.getProductId()).orElseThrow(), form.getProductCount());

//        배송지 파트
//        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
//        Delivery delivery = new Delivery(member, address);
//
//        if (deliveryRepository.existsDistinctByAddressAndMember(address, member)) {
//            deliveryRepository.save(delivery);
//        }

        Order order = Order.createOrder(member, new Delivery(member, new Address()), orderItems);

        orderItemRepository.save(orderItems);
        orderRepository.save(order);

        return order.getId();
    }

    public void updateOrderNow(Long orderId, OrderNowForm form) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        //속도 향상하려면 그대로면, 안 만들게 해주면 될듯.
        OrderItem findOrderItem = orderRepository.findById(orderId).orElseThrow().getOrderItems().get(0);
        findOrderItem.setCount(form.getProductCount());
        findOrderItem.setAllPrice(form.getProductAllPrice());

//        OrderItem orderItem = new OrderItem(productRepository.findById(form.getProductId()).orElseThrow(), form.getProductCount());

        //추후 기존 딜러버리를 넣어주는 것도 만들어줫으면 좋겠음.
        Address updateAddress = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        Delivery updateDelivery = new Delivery(member, updateAddress);

        if (!deliveryRepository.existsDistinctByAddressAndMember(updateAddress, member)) {
            log.info("add Delivery");
            deliveryRepository.save(updateDelivery);
        }

        order.setMember(member);
        order.setDelivery(updateDelivery);
    }

    public void deleteOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByUpdatedTimeDesc();
    }

    public List<Order> getOrderList(int page, Integer perPageNum, List<Order> orders) {
        if (perPageNum == null) {
            perPageNum = 10;
        }
        int startOrder= (page - 1) * perPageNum;
        int endOder = Math.min(page * perPageNum, orders.size());

        log.info("= {}, = {}", page, perPageNum);

        return orders.subList(startOrder, endOder);
    }


    public OrderNowForm orderToOrderNowForm(Order order) {
        return new OrderNowForm(order.getId(), order.getMember().getId(), order.getOrderItems().get(0).getProduct().getId(),
                order.getOrderItems().get(0).getCount(),
                order.getDelivery().getAddress().getCity(), order.getDelivery().getAddress().getStreet(), order.getDelivery().getAddress().getZipcode(),
                order.getTotalPrice());
    }

    public List<OrderNowForm> ordersToOrderNowForms(List<Order> orderList) {
        return orderList.stream()
                .map(o -> new OrderNowForm(o.getId(), o.getMember().getId(), o.getOrderItems().get(0).getProduct().getId(),
                        o.getOrderItems().get(0).getCount(),
                        o.getDelivery().getAddress().getCity(), o.getDelivery().getAddress().getStreet(), o.getDelivery().getAddress().getZipcode(),
                        o.getTotalPrice()))
                .collect(Collectors.toList());
    }

    public List<Order> findSearchedAndSortedOrder(OrderSearchCond cond) {
        return orderQueryRepository.findSearchedAndSortedOrder(cond);
    }

    public Optional<Order> findById(long orderId) {
        return orderRepository.findById(orderId);
    }
}
