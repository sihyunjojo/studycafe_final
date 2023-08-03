package project.studycafe.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.contoller.form.CartForm;
import project.studycafe.contoller.form.OrderForm;
import project.studycafe.contoller.form.OrderNowForm;
import project.studycafe.contoller.form.OrderUserForm;
import project.studycafe.domain.*;
import project.studycafe.repository.OrderSearchCond;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.service.OrderService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

        //클라이언트 처리
        model.addAttribute("orders", orderList);
        model.addAttribute("pageMaker", pageMaker);

        return "order/orders";
    }

    @GetMapping("/search")
    public String searchOrders(@ModelAttribute("orderSearch") OrderSearchCond orderSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        log.info("orderSearch = {}", orderSearch);
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
            log.info("searchtype = {}", orderSearch.getMinCreatedTime().getClass());
        }

        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("orders", findOrderList);
        log.info("orderSearch = {}", orderSearch);
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
    @PostMapping("/add/{productId}/now")
    public String addOrderNow(@Login Member loginMember, @PathVariable Long productId, OrderNowForm form) {
        log.info("add loginMember = {}", form);
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/product/" + productId;
        }
        long orderId = orderService.addOrderNow(form);
        return "redirect:/order/edit/" + orderId
                + "/user";
    }

    @PostMapping("/add/cart")
    public String addOrderCart(@Login Member loginMember, CartForm form) throws IOException, NoSuchAlgorithmException {
        log.info("cartForm = {}", form);
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/cart";
        }
        long orderId = orderService.addOrderCart(form);
        //결제 창으로 넘겨줘야함.
        return "redirect:/order/edit/" + orderId
                +"/user";
    }


    //master control
    @GetMapping("/edit/{orderId}")
    public String editForm(@PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow();
        log.info("order = {}", order);
        model.addAttribute("order", order);
        return "order/editOrderForm";
    }
    @GetMapping("/edit/{orderId}/user")
    public String editOrderCartForm(@PathVariable long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow();
        model.addAttribute("order", order);
        return "order/editOrderUserForm";
    }


    @PostMapping("/edit/{orderId}")
    public String editOrderNow(@PathVariable Long orderId, OrderForm form) {
        log.info("form = {}", form);
        orderService.updateOrder(orderId, form);
        return "redirect:/order/" + orderId;
    }
    @PostMapping("/edit/{orderId}/user")
    public String editOrderNow(@PathVariable long orderId, OrderUserForm form) {
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
