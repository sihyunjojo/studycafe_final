package project.studycafe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.*;
import project.studycafe.domain.*;
import project.studycafe.exception.NotFindOrderItemException;
import project.studycafe.repository.*;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.repository.product.JpaProductRepository;

import java.util.*;
import java.util.stream.Collectors;

import static project.studycafe.domain.DeliveryStatus.*;
import static project.studycafe.domain.Order.createOrder;
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

    //여기선 배송지 입력 안함. (정보만 받아서 editForm 으로 넘겨줘서 배송지 입력)
    public long addOrderNow(OrderNowForm form) {
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        OrderItem orderItem = createOrderItem(productRepository.findById(form.getProductId()).orElseThrow(), form.getProductCount());

        Order order = createOrder(member, orderItem);

        //자동으로 해줌
//        orderItemRepository.save(orderItem);
        orderRepository.save(order);

        return order.getId();
    }

    public long addOrderCart(CartForm form) {
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();
        List<OrderItem> orderItems = new ArrayList<>();

        List<CartProductForm> cartProductForms = form.getCartProductList();
        for (CartProductForm cartProductForm : cartProductForms) {
            if (cartProductForm.isPurchasedCheck()) {
                OrderItem orderItem = createOrderItem(productRepository.findById(cartProductForm.getProductId()).orElseThrow(), cartProductForm.getCount());
                orderItems.add(orderItem);
                orderItemRepository.save(orderItem);
            }
        }

        if (orderItems.size() == 0) {
            throw new NotFindOrderItemException("장바구니에 구매할 상품을 체크 후 구매하기 버튼을 눌러주세요");
        }
        Order order = createOrder(member, orderItems);
        orderRepository.save(order);
        return order.getId();
    }


    // user 전용
    public void updateOrder(Long orderId, OrderForm form) {
        log.info("form = {}", form);
        Order order = orderRepository.findById(orderId).orElseThrow();
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();


        int updateOrderTotalPrice = 0;
        for (OrderItemForm updateOrderItemform : form.getOrderItems()) {
            if (!updateOrderItemform.isEmpty()) {
                updateOrderTotalPrice += updateOrderItemform.getAllPrice();
            }
        }

        order.setTotalPrice(updateOrderTotalPrice);

        Address updateAddress = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Delivery updateDelivery = new Delivery(member, updateAddress, stringToDeliveryStatus(form.getDeliveryStatus()));
        if (!deliveryRepository.existsDistinctByAddressAndMember(updateAddress, member)) {
            log.info("add Delivery");
            deliveryRepository.save(updateDelivery);
        }
        order.setDelivery(updateDelivery);

        order.setStatus(stringToOrderStatus(form.getOrderStatus()));
    }

    public void updateUserOrder(Long orderId, OrderUserForm form) {
        log.info("form = {}", form);
        Order order = orderRepository.findById(orderId).orElseThrow();
        Member member = memberRepository.findById(form.getMemberId()).orElseThrow();


        int updateOrderTotalPrice = 0;
        for (OrderItemForm updateOrderItemform : form.getOrderItems()) {
            if (!updateOrderItemform.isEmpty()) {
                updateOrderTotalPrice += updateOrderItemform.getAllPrice();
            }
        }

        order.setTotalPrice(updateOrderTotalPrice);

        Address updateAddress = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Delivery updateDelivery = new Delivery(member, updateAddress, READY);
        if (!deliveryRepository.existsDistinctByAddressAndMember(updateAddress, member)) {
            log.info("add Delivery");
            deliveryRepository.save(updateDelivery);
        }
        order.setDelivery(updateDelivery);
    }

    public void deleteOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByUpdatedTimeDesc();
    }

    public void cancelOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(CANCEL);
    }

    public List<Order> getOrderList(int page, Integer perPageNum, List<Order> orders) {
        if (perPageNum == null) {
            perPageNum = 10;
        }

        int startOrder = (page - 1) * perPageNum;
        int endOder = Math.min(page * perPageNum, orders.size());

        log.info("= {}, = {}", page, perPageNum);

        return orders.subList(startOrder, endOder);
    }


    public Optional<Order> findById(long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> findSearchedAndSortedOrder(OrderSearchCond cond) {
        return orderQueryRepository.findSearchedAndSortedOrder(cond);
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

    public List<Order> findByMember(Member loginMember) {
        return orderRepository.findAllByMember(loginMember);
    }
}

