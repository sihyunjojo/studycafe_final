package project.studycafe.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.app.domain.Cart;
import project.studycafe.app.domain.member.Member;
import project.studycafe.helper.resolver.argumentresolver.Login;
import project.studycafe.app.service.cart.CartService;

import java.util.Optional;


@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니 보여주기
    @GetMapping
    public String cart(@Login Member loginmember, Model model) {
        Cart cart;
        Optional<Cart> findcart = cartService.findByMember(loginmember);

        if (findcart.isEmpty()) {
            cart = cartService.addCart(loginmember).orElseThrow();
        }else {
            cart = findcart.orElseThrow();
        }

        log.info("cart ={}", cart);
        model.addAttribute("cart", cart);

        return "cart/cart";
    }

    // 장바구니에 상품 추가
    @GetMapping("/{productId}/add")
    public String addCartProduct(@Login Member loginmember, @PathVariable long productId) {
        cartService.addCartProduct(loginmember, productId);
        return "redirect:/product/" + productId;
    }

    @GetMapping("/{itemId}/edit/up")
    public String editUp(@Login Member loginmember, @PathVariable long itemId) {
        cartService.editUpQuantityCartProduct(loginmember, itemId);
        return "redirect:/cart";
        // 여기도 비동기 코드도 해야할거 같기도하고,,
        // 좋아요 같이 해야할거같은데
    }

    @GetMapping("/{itemId}/edit/down")
    public String editDown(@Login Member loginmember, @PathVariable long itemId) {
        cartService.editDownQuantityCartProduct(loginmember, itemId);
        return "redirect:/cart";
    }

    @PostMapping("/{itemId}/delete")
    public String delete(@Login Member loginmember,@PathVariable long itemId) {
        cartService.deleteCartProduct(loginmember, itemId);
        return "redirect:/cart";
        // 여기도 비동기 코드도 해야할거 같기도하고,,
        // 좋아요 같이 해야할거같은데
    }
}
