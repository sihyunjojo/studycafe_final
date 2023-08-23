package project.studycafe.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.CartForm;
import project.studycafe.app.controller.form.CartProductForm;
import project.studycafe.app.controller.form.OrderItemForm;
import project.studycafe.app.domain.Address;
import project.studycafe.app.domain.Delivery;
import project.studycafe.app.domain.Order;
import project.studycafe.app.repository.DeliveryRepository;
import project.studycafe.app.repository.OrderItemRepository;
import project.studycafe.app.domain.enums.status.DeliveryStatus;
import project.studycafe.app.domain.enums.status.OrderStatus;
import project.studycafe.app.controller.form.order.OrderForm;
import project.studycafe.app.controller.form.order.OrderNowForm;
import project.studycafe.app.controller.form.order.OrderUserForm;
import project.studycafe.app.service.dto.searchDto.OrderSearchCond;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.OrderItem;
import project.studycafe.helper.exception.order.NotFindOrderItemException;
import project.studycafe.app.repository.member.JpaMemberRepository;
import project.studycafe.app.repository.order.OrderQueryRepository;
import project.studycafe.app.repository.order.OrderRepository;
import project.studycafe.app.repository.product.JpaProductRepository;

import java.util.*;

import static project.studycafe.app.domain.Address.createAddress;
import static project.studycafe.app.domain.Delivery.createDelivery;
import static project.studycafe.app.domain.Order.createOrder;
import static project.studycafe.app.domain.enums.status.DeliveryStatus.*;
import static project.studycafe.app.domain.enums.status.OrderStatus.*;
import static project.studycafe.app.domain.product.OrderItem.createOrderItem;


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

        Address updateAddress = createAddress(form.getCity(), form.getStreet(), form.getZipcode());

        Delivery updateDelivery = createDelivery(member, updateAddress);
        if (!deliveryRepository.existsDistinctByAddressAndMember(updateAddress, member)) {
            log.info("add Delivery");
            deliveryRepository.save(updateDelivery);
        }

        order.updateOrder(updateOrderTotalPrice, updateAddress, stringToOrderStatus(form.getOrderStatus()), stringToDeliveryStatus(form.getDeliveryStatus()));

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

        Address updateAddress = createAddress(form.getCity(), form.getStreet(), form.getZipcode());

        Delivery updateDelivery = createDelivery(member, updateAddress);
        if (!deliveryRepository.existsDistinctByAddressAndMember(updateAddress, member)) {
            log.info("add Delivery");
            deliveryRepository.save(updateDelivery);
        }

        order.updateOrder(updateOrderTotalPrice, updateAddress);

    }

    public void deleteOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByUpdatedTimeDesc();
    }

    public void cancelOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.updateOrderStatus(CANCEL);
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
                return OrderStatus.COMPLETE;
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
            case "COMPLETE":
                return DeliveryStatus.COMPLETE;
            default:
                return null;
        }
    }

    public List<Order> findByMember(Member loginMember) {
        return orderRepository.findAllByMember(loginMember);
    }
}

