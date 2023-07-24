package project.studycafe.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.contoller.form.BoardCreateForm;
import project.studycafe.contoller.form.BoardForm;
import project.studycafe.contoller.form.BoardUpdateForm;
import project.studycafe.contoller.form.OrderNowForm;
import project.studycafe.domain.*;
import project.studycafe.repository.OrderSearchCond;
import project.studycafe.repository.board.board.dto.BoardSearchCond;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.service.OrderService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private static final int BASIC_PER_PAGE_NUM = 10;    // 페이지당 보여줄 게시판 개수

    //ordernow의 경우에만 만듬.
    @GetMapping()
    public String orders(@ModelAttribute("orderSearch") OrderSearchCond orderSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Order> orders = orderService.findAllOrders();
        orderSearch.setPerPageNum(BASIC_PER_PAGE_NUM);

        List<Order> orderList = orderService.getOrderList(page, BASIC_PER_PAGE_NUM, orders);

        PageMaker pageMaker = new PageMaker(orders.size(), page, BASIC_PER_PAGE_NUM);

        //클랄이언트 처리
        List<OrderNowForm> orderForms = orderService.ordersToOrderNowForms(orderList);
        model.addAttribute("orders", orderForms);
        model.addAttribute("pageMaker", pageMaker);

        return "order/orders";

    }

    @GetMapping("/search")
    public String searchOrders(@ModelAttribute("orderSearch") OrderSearchCond orderSearch, @RequestParam(required = false, defaultValue = "1") int page, Model model) {
        List<Order> findOrders = orderService.findSearchedAndSortedOrder(orderSearch);

        List<Order> findOrderList = orderService.getOrderList(page, orderSearch.getPerPageNum(), findOrders);

        PageMaker pageMaker = new PageMaker(findOrders.size(), page, orderSearch.getPerPageNum());

        List<OrderNowForm> orderNowForms = orderService.ordersToOrderNowForms(findOrderList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("orders", orderNowForms);
        model.addAttribute("orderSearch", orderNowForms);

        return "order/orders";
    }


    @GetMapping("/{orderId}")
    public String order(@PathVariable long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow();
        OrderNowForm orderNowForm = orderService.orderToOrderNowForm(order);


        model.addAttribute("order", orderNowForm);
        return "order/order";
    }

    @PostMapping("/add/{productId}")
    public String addOrderNow(@Login Member loginMember, @PathVariable Long productId ,OrderNowForm form) throws IOException, NoSuchAlgorithmException {
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/product/" + productId;
        }
        orderService.addOrderNow(form);

        return "redirect:/order"; // 일단 home으로 보내주자 나중에 board목록으로 보내주고
    }

    @GetMapping("/{orderId}/edit")
    public String editForm(@PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId).orElseThrow();
        OrderNowForm orderNowForm  = orderService.orderToOrderNowForm(order);

        model.addAttribute("order", orderNowForm);
        return "order/editOrderForm";
    }

    @PostMapping("/{orderId}/edit")
    public String editOrderNow(OrderNowForm form, @PathVariable Long orderId) throws IOException, NoSuchAlgorithmException {
        orderService.updateOrderNow(orderId, form);
        return "redirect:/order/" + orderId;
    }

    @GetMapping("/{orderId}/delete")
    public String delete(@PathVariable long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/order";
    }

}
