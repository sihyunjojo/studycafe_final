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

//        Order order = Order.createOrder(member, new Delivery(member, new Address(), READY), orderItem);
        Order order = createOrder(member, orderItem);

        orderItemRepository.save(orderItem);
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
        Order order = createOrder(member,orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    public void updateOrderNow(Long orderId, OrderNowForm form) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        //속도 향상하려면 그대로면, 안 만들게 해주면 될듯.
        OrderItem findOrderItem = orderRepository.findById(orderId).orElseThrow().getOrderItems().get(0);
        // 새 count 값이 들어왔을 수 있으니 다시 계산해서 넣어줘야함.
        findOrderItem.setCount(form.getProductCount());
        findOrderItem.setAllPrice(form.getProductCount() * productRepository.findById(form.getProductId()).orElseThrow().getPrice());

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

    public void updateOrderCart(Long orderId, OrderCartForm form) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        //속도 향상하려면 그대로면, 안 만들게 해주면 될듯.
        List<OrderItem> findOrderItems = orderRepository.findById(orderId).orElseThrow().getOrderItems();
        Map<Long, OrderItem> orderItemMap = new HashMap<>();
        for (OrderItem findOrderItem : findOrderItems) {
            orderItemMap.put(findOrderItem.getProduct().getId(), findOrderItem);
        }

        log.info("orderItemMap ={}", orderItemMap);
        //물건 겹쳐서 안 들어옴.
        List<OrderItemForm> orderItemsForm = form.getOrderItems();

        log.info("orderItemsForm = {}", orderItemsForm);
        for (OrderItemForm orderItemForm : orderItemsForm) {
            long orderItemId = orderItemForm.getId();

            OrderItem findOrderItem = orderItemMap.get(orderItemId);

            if (findOrderItem != null) {
                findOrderItem.setCount(orderItemForm.getCount());
                findOrderItem.setAllPrice(orderItemForm.getCount() * productRepository.findById(orderItemId).orElseThrow().getPrice());
            } else {
                throw new NotFindOrderItemException("The orderItem could not be found.");
            }
        }

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
        int startOrder = (page - 1) * perPageNum;
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

    public void cancelOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(CANCEL);
    }
}

