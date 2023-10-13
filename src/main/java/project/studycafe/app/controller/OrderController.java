package project.studycafe.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.app.controller.form.CartForm;
import project.studycafe.app.controller.form.order.OrderForm;
import project.studycafe.app.controller.form.order.OrderNowForm;
import project.studycafe.app.controller.form.order.OrderUserForm;
import project.studycafe.app.domain.Order;
import project.studycafe.app.domain.enums.status.OrderStatus;
import project.studycafe.app.service.dto.PageMaker;
import project.studycafe.app.domain.enums.MemberLevel;
import project.studycafe.app.service.dto.searchDto.OrderSearchCond;
import project.studycafe.app.domain.member.Member;
import project.studycafe.helper.exception.order.DoNotEditByDeliveryStartedException;
import project.studycafe.helper.resolver.argumentresolver.Login;
import project.studycafe.app.service.OrderService;

import java.util.List;

import static project.studycafe.app.domain.enums.status.OrderStatus.WAIT;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private static final int BASIC_PER_PAGE_NUM = 10;    // 페이지당 보여줄 게시판 개수

    @GetMapping()
    public String orders(@Login Member loginMember, @ModelAttribute("orderSearch") OrderSearchCond orderSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Order> orders;
        if (loginMember.getMemberLevel().equals(MemberLevel.MASTER)) {
            orders = orderService.findAllOrders();
            model.addAttribute("memberLevel", "모든사람");
        } else {
            orders = orderService.findByMember(loginMember);
            model.addAttribute("memberLevel", loginMember.getNickname() +"의");
        }
        orderSearch.setPerPageNum(BASIC_PER_PAGE_NUM);

        List<Order> orderList = orderService.getOrderList(page, BASIC_PER_PAGE_NUM, orders);
        PageMaker pageMaker = new PageMaker(orders.size(), page, BASIC_PER_PAGE_NUM);


        model.addAttribute("orders", orderList);
        model.addAttribute("pageMaker", pageMaker);

        return "order/orders";
    }

    @GetMapping("/search")
    public String searchOrders(@ModelAttribute("orderSearch") OrderSearchCond orderSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Order> findOrders = orderService.findSearchedAndSortedOrder(orderSearch);

        List<Order> findOrderList = orderService.getOrderList(page, orderSearch.getPerPageNum(), findOrders);

        PageMaker pageMaker = new PageMaker(findOrders.size(), page, orderSearch.getPerPageNum());

        if (orderSearch.getSort() != null) {
            if (orderSearch.getSort().equals("orderIdUp")) {
                orderSearch.setSort("orderIdDown");
            } else if (orderSearch.getSort().equals("orderIdDown")) {
                orderSearch.setSort("orderIdUp");
            } else if (orderSearch.getSort().equals("orderStatusDown")) {
                orderSearch.setSort("orderStatusUp");
            } else if (orderSearch.getSort().equals("orderStatusUp")) {
                orderSearch.setSort("orderStatusDown");
            }
        }

        if (orderSearch.getMinCreatedTime() != null) {
            log.info("search type = {}", orderSearch.getMinCreatedTime().getClass());
        }

        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("orders", findOrderList);
        model.addAttribute("orderSearch", orderSearch);

        return "order/orders";
    }

    @GetMapping("/{orderId}")
    public String order(@PathVariable long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow();
        log.info("order ={}", order);
        model.addAttribute("order", order);
        return "order/order";
    }


    //product.html에서 구매하면 member,product 로 order 만들어서 추가정보 입력하게해주게 보내줌.
    @PostMapping("/{productId}/add/now")
    public String addOrderNow(@Login Member loginMember, @PathVariable Long productId, OrderNowForm form) {
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/product/" + productId;
        }

        long orderId = orderService.addOrderNow(form);
        return "redirect:/order/" + orderId + "/edit/user";
    }

    @PostMapping("/add/cart")
    public String addOrderCart(@Login Member loginMember, CartForm form) {
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/cart";
        }

        long orderId = orderService.addOrderCart(form);
        //결제 창으로 넘겨줘야함.
        return "redirect:/order/" + orderId + "/edit/user";
    }


    //master control
    @GetMapping("/{orderId}/edit")
    public String editForm(@Login Member loginMember, @PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow();
        model.addAttribute("order", order);

        if (loginMember.getMemberLevel().equals(MemberLevel.MASTER)) {
            return "order/editOrderForm";
        }

        if (loginMember.getMemberLevel().equals(MemberLevel.USER)) {
            if (order.getOrderStatus().equals(WAIT)) {
                return "order/editOrderUserForm";
            }
            throw new DoNotEditByDeliveryStartedException("배송이 이미 시작되었거나 배송이 완료되어서, 정보를 변경 하실 수 없습니다.");
        }

        return "order/editOrderUserForm";
    }

    @GetMapping("/{orderId}/edit/user")
    public String editOrderUserForm(@PathVariable long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow();
        model.addAttribute("order", order);
        return "order/editOrderUserForm";
    }


    @PostMapping("/{orderId}/edit")
    public String editOrder(@PathVariable Long orderId, OrderForm form) {
        orderService.updateOrder(orderId, form);
        return "redirect:/order/" + orderId;
    }
    @PostMapping("/{orderId}/edit/user")
    public String editOrderUser(@PathVariable long orderId, OrderUserForm form) {
        log.info("OrderCartForm = {}", form);
        orderService.updateUserOrder(orderId, form);
        // 지금은 오더화면으로 넘겨주지만, 나중에는 결제창으로 넘겨줘야함.
        return "redirect:/order/" + orderId;
    }


    @GetMapping("/{orderId}/delete")
    public String delete(@PathVariable long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/order";
    }

    @PostMapping("/{orderId}/delete")
    public String deletePost(@PathVariable long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/order";
    }

    @PostMapping("/{orderId}/cancel")
    public String cancel(@PathVariable long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/order";
    }
}
