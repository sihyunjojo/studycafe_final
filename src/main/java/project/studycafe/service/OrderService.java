package project.studycafe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.OrderForm;
import project.studycafe.contoller.form.OrderItemForm;
import project.studycafe.contoller.form.OrderNowForm;
import project.studycafe.domain.*;
import project.studycafe.repository.*;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.repository.product.JpaProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static project.studycafe.domain.DeliveryStatus.*;
import static project.studycafe.domain.OrderItem.createOrderItem;
import static project.studycafe.domain.OrderStatus.*;

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

        Order order = Order.createOrder(member, new Delivery(member, new Address(), READY), orderItems);

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

        //추후 기존 address를 테이블로 만들어서 address 선택해서 넣어주는 것도 있으면 좋을듯.?
        Address updateAddress = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        Delivery updateDelivery = new Delivery(member, updateAddress, READY);

        if (!deliveryRepository.existsDistinctByAddressAndMember(updateAddress, member)) {
            log.info("add Delivery");
            deliveryRepository.save(updateDelivery);
        }

        order.setMember(member);
        order.setDelivery(updateDelivery);
    }

    // master 전용?
    public void updateOrder(Long orderId, OrderForm form) {
        log.info("form = {}", form);
        Order order = orderRepository.findById(orderId).orElseThrow();
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        //속도 향상하려면 그대로면, 안 만들게 해주면 될듯.
        //
//        List<OrderItem> findOrderItems = orderRepository.findById(orderId).orElseThrow().getOrderItems();
//        log.info("findOrderItems = {}", findOrderItems);

//        for (OrderItem findOrderItem : findOrderItems) {
//            log.info("findOrderItems = {}", findOrderItem);
            // 리스트로 관리해서 하나씩 떼어줄 수 없기떄문에??? 해줘야하나??
//            order.removeOrderItems(findOrderItem);
//            orderItemRepository.delete(findOrderItem);
//        }

//        int newOrderTotalPrice = 0;

//         추후 아이템을 추가할 수 있게 되면 또 바꿔야함.
//        List<OrderItemForm> updateOrderItems = form.getOrderItems();
//        List<OrderItem> newOrderItems = new ArrayList<>();
//
//        for (OrderItemForm updateOrderItem : updateOrderItems) {
//            Product product = productRepository.findById(updateOrderItem.getId()).orElseThrow();
//            OrderItem newOrderItem = createOrderItem(product, updateOrderItem.getCount());
//            log.info("orderItem = {}", newOrderItem);
//            newOrderItem.setOrder(order); //setOrderItems()에서해줌
//            newOrderTotalPrice += newOrderItem.getAllPrice();
//
//            log.info("save orderItem");
//            orderItemRepository.save(newOrderItem);

            // 자동으로 setOrder()해줌
//            newOrderItems.add(newOrderItem);
//        }

//        order.setOrderItems(newOrderItems);
//        log.info("change orderItems");


//        order.setTotalPrice(newOrderTotalPrice);
//        log.info("change totalPrice");

        // 추후에 클라이언트에서 계산 가능해지면,
//        order.setOrderTotalPrice(form.getOrderTotalPrice());


        // 총가격도 업데이트 문제 없음.
        // 아래는 성공 문제 없음.
        //추후 기존 address 를 테이블로 만들어서 address 선택해서 넣어주는 것도 있으면 좋을듯.?
        Address updateAddress = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Delivery updateDelivery = new Delivery(member, updateAddress, stringToDeliveryStatus(form.getDeliveryStatus()));
        if (!deliveryRepository.existsDistinctByAddressAndMember(updateAddress, member)) {
            log.info("add Delivery");
            deliveryRepository.save(updateDelivery);
        }

        order.setDelivery(updateDelivery);
        order.setStatus(stringToOrderStatus(form.getOrderStatus()));
    }

    public void updateOrderItems(List<OrderItemForm> updateOrderItems, Long orderId) {
        List<OrderItem> newOrderItems = new ArrayList<>();
        int newOrderTotalPrice = 0;

        Order order = orderRepository.findById(orderId).orElseThrow();

        for (OrderItemForm updateOrderItemform : updateOrderItems) {
            if (!updateOrderItemform.isEmpty()) {
                Product product = productRepository.findById(updateOrderItemform.getId()).orElseThrow();
                OrderItem newOrderItem = createOrderItem(product, updateOrderItemform.getCount());
                newOrderItem.setOrder(order); //order.setOrderItems()도해줌
                newOrderTotalPrice += newOrderItem.getAllPrice();

                orderItemRepository.save(newOrderItem);
                log.info("save orderItem");

                newOrderItems.add(newOrderItem);
                log.info("add newOrderItem = {}", newOrderItem);
            }

            log.info("save orderItems");
        }
        order.setTotalPrice(newOrderTotalPrice);
    }

    private OrderStatus stringToOrderStatus(String orderStatus) {
        switch (orderStatus) {
            case "WAIT":
                return WAIT;
            case "DELIVERING":
                return OrderStatus.DELIVERING;
            case "COMPLETE":
                return COMPLETE;
            case "CANCEL":
                return CANCEL;
            default:
                return null;
        }

    }
    private DeliveryStatus stringToDeliveryStatus(String deliveryStatus) {
        switch (deliveryStatus) {
            case "READY":
                return READY;
            case "DELIVERING":
                return DeliveryStatus.DELIVERING;
            case "COMP":
                return COMP;
            default:
                return null;
        }
    }

        public void deleteOrder (long orderId){
            orderRepository.deleteById(orderId);
        }

        public List<Order> findAllOrders () {
            return orderRepository.findAllByOrderByUpdatedTimeDesc();
        }

        public List<Order> getOrderList ( int page, Integer perPageNum, List < Order > orders){
            if (perPageNum == null) {
                perPageNum = 10;
            }
            int startOrder = (page - 1) * perPageNum;
            int endOder = Math.min(page * perPageNum, orders.size());

            log.info("= {}, = {}", page, perPageNum);

            return orders.subList(startOrder, endOder);
        }


        public OrderNowForm orderToOrderNowForm (Order order){
            return new OrderNowForm(order.getId(), order.getMember().getId(), order.getOrderItems().get(0).getProduct().getId(),
                    order.getOrderItems().get(0).getCount(),
                    order.getDelivery().getAddress().getCity(), order.getDelivery().getAddress().getStreet(), order.getDelivery().getAddress().getZipcode(),
                    order.getTotalPrice());
        }

        public List<OrderNowForm> ordersToOrderNowForms (List < Order > orderList) {
            return orderList.stream()
                    .map(o -> new OrderNowForm(o.getId(), o.getMember().getId(), o.getOrderItems().get(0).getProduct().getId(),
                            o.getOrderItems().get(0).getCount(),
                            o.getDelivery().getAddress().getCity(), o.getDelivery().getAddress().getStreet(), o.getDelivery().getAddress().getZipcode(),
                            o.getTotalPrice()))
                    .collect(Collectors.toList());
        }

        public List<Order> findSearchedAndSortedOrder (OrderSearchCond cond){
            return orderQueryRepository.findSearchedAndSortedOrder(cond);
        }

        public Optional<Order> findById ( long orderId){
            return orderRepository.findById(orderId);
        }

}
