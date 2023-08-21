package project.studycafe.app.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.CartProductForm;
import project.studycafe.app.domain.Cart;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.product.CartProduct;
import project.studycafe.app.domain.product.Product;
import project.studycafe.app.repository.cart.JpaCartProductRepository;
import project.studycafe.app.repository.cart.JpaCartRepository;
import project.studycafe.app.repository.member.JpaMemberRepository;
import project.studycafe.app.repository.product.JpaProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static project.studycafe.app.domain.Cart.createCart;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final JpaCartRepository cartRepository;
    private final JpaCartProductRepository cartProductRepository;
    private final JpaProductRepository productRepository;
    private final JpaMemberRepository memberRepository;

    public Optional<Cart> addCart(Member member) {
        Cart cart = createCart(member);
        cartRepository.save(cart);

        return Optional.of(cart);
    }

    public void addCartProduct(Member member, long itemId) {
        Optional<Cart> findCart = cartRepository.findFirstByMemberId(member.getId());

        if (findCart.isEmpty()) {
            Optional<Cart> newCart = addCart(member);
            findCart = newCart;
        }

        Product findproduct = productRepository.findById(itemId).orElseThrow();

        // 카트안에 제품이 있는지
        Optional<CartProduct> existingCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findCart.orElseThrow().getId(), findproduct.getId());

        if (existingCartProduct.isEmpty()) {
            CartProduct cartProduct = new CartProduct();

            cartProduct.setCart(findCart.orElseThrow());
            cartProduct.setProduct(findproduct);
            cartProduct.setCount(1); // 추후에 값을 받아와서 해야함.
            cartProduct.setTotalPrice(findproduct.getPrice());
            cartProductRepository.save(cartProduct);
        }
    }

    public void editUpQuantityCartProduct(Member member, long itemId) {
        Long memberId = memberRepository.findFirstByUserLoginId(member.getUserLoginId()).orElseThrow().getId();

        Cart findcart = cartRepository.findFirstByMemberId(memberId).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        CartProduct findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart.getId(), findproduct.getId()).orElseThrow();
        findCartProduct.setCount(findCartProduct.getCount() + 1);
    }

    public void editDownQuantityCartProduct(Member member, long itemId) {
        Long memberId = memberRepository.findFirstByUserLoginId(member.getUserLoginId()).orElseThrow().getId();

        Cart findcart = cartRepository.findFirstByMemberId(memberId).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        CartProduct findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart.getId(), findproduct.getId()).orElseThrow();
        if (findCartProduct.getCount() > 0) {
            findCartProduct.setCount(findCartProduct.getCount() - 1);
        }

    }



    public void deleteCartProduct(Member member, long itemId) {
        Long memberId = memberRepository.findFirstByUserLoginId(member.getUserLoginId()).orElseThrow().getId();

        Cart findcart = cartRepository.findFirstByMemberId(memberId).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        Optional<CartProduct> findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart.getId(), findproduct.getId());
        cartProductRepository.delete(findCartProduct.orElseThrow());

    }

    public List<CartProductForm> cartProductToCartProductForm(List<CartProduct> cartProducts) {
        List<CartProductForm> cartProductForms = new ArrayList<>();

        for (CartProduct cartProduct : cartProducts) {
            CartProductForm cartProductForm = new CartProductForm();
            Product findProduct = productRepository.findById(cartProduct.getProduct().getId()).orElseThrow();

            log.info("findproduct ={}", findProduct);
//            cartProductForm.setCheck(false);
            cartProductForm.setProductId(findProduct.getId());
            cartProductForm.setCount(cartProduct.getCount());
            cartProductForm.setTotalPrice(cartProduct.getTotalPrice());
            cartProductForms.add(cartProductForm);
        }

        return cartProductForms;
    }

    public Cart findByMemberId(Long memberId) {
        return cartRepository.findFirstByMemberId(memberId).orElseThrow();

    }
}
