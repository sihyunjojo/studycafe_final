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
import project.studycafe.app.repository.product.JpaProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static project.studycafe.app.domain.Cart.createCart;
import static project.studycafe.app.domain.product.CartProduct.createCartProduct;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final JpaCartRepository cartRepository;
    private final JpaCartProductRepository cartProductRepository;
    private final JpaProductRepository productRepository;

    public Optional<Cart> addCart(Member member) {
        Cart cart = createCart(member);
        cartRepository.save(cart);

        return Optional.of(cart);
    }

    public void addCartProduct(Member member, long itemId) {
        Cart cart;
        Optional<Cart> findCart = cartRepository.findFirstByMember(member);

        if (findCart.isEmpty()) {
            log.info("create cart");
            cart = addCart(member).orElseThrow();
            log.info("cart = {}", cart);
        } else{
            cart = findCart.orElseThrow();
        }

        Product findproduct = productRepository.findById(itemId).orElseThrow();

        // 카트안에 제품이 있는지
        Optional<CartProduct> existingCartProduct = cartProductRepository.findFirstByCartIdAndProductId(cart, findproduct);

        if (existingCartProduct.isEmpty()) {
            CartProduct cartProduct = createCartProduct(cart, findproduct);
            cartProductRepository.save(cartProduct);
        }
    }

    public void editUpQuantityCartProduct(Member member, long itemId) {
        Cart findcart = cartRepository.findFirstByMember(member).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        //


        CartProduct findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart, findproduct).orElseThrow();
        findCartProduct.setCount(findCartProduct.getCount() + 1);
    }

    public void editDownQuantityCartProduct(Member member, long itemId) {
        Cart findcart = cartRepository.findFirstByMember(member).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        CartProduct findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart, findproduct).orElseThrow();
        if (findCartProduct.getCount() > 0) {
            findCartProduct.setCount(findCartProduct.getCount() - 1);
        }

    }



    public void deleteCartProduct(Member member, long itemId) {
        Cart findcart = cartRepository.findFirstByMember(member).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        Optional<CartProduct> findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart, findproduct);
        cartProductRepository.delete(findCartProduct.orElseThrow());

    }

    public List<CartProductForm> cartProductToCartProductForm(List<CartProduct> cartProducts) {
        List<CartProductForm> cartProductForms = new ArrayList<>();

        for (CartProduct cartProduct : cartProducts) {
            CartProductForm cartProductForm = new CartProductForm();
            Product findProduct = productRepository.findById(cartProduct.getProduct().getId()).orElseThrow();

            log.info("findproduct ={}", findProduct);
//            cartProductForm.setCheck(false);
            cartProductForm.setId(findProduct.getId());
            cartProductForm.setCount(cartProduct.getCount());
            cartProductForm.setTotalPrice(cartProduct.getTotalPrice());
            cartProductForms.add(cartProductForm);
        }

        return cartProductForms;
    }

    public Optional<Cart> findByMember(Member member) {
        return cartRepository.findFirstByMember(member);
    }

    public Optional<Cart> findById(Long cartId) {
        return cartRepository.findById(cartId);
    }

}
