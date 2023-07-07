package project.studycafe.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.Cart;
import project.studycafe.domain.CartProduct;
import project.studycafe.domain.Member;
import project.studycafe.domain.Product;
import project.studycafe.repository.cart.JpaCartProductRepository;
import project.studycafe.repository.cart.JpaCartRepository;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.repository.product.JpaProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final JpaCartRepository cartRepository;
    private final JpaCartProductRepository cartProductRepository;
    private final JpaProductRepository productRepository;
    private final JpaMemberRepository memberRepository;

    public List<CartProduct>  findCartProducts(Member member) {
        Long memberId = memberRepository.findFirstByUserLoginId(member.getUserLoginId()).orElseThrow().getId();
        Optional<Cart> cart = cartRepository.findByMemberId(memberId);

        if (cart.isEmpty()) {
            Optional<Cart> newCart = addCart(member);
            cart = newCart;
        }

        List<CartProduct> cartProducts = cartProductRepository.findAllByCartId(cart.orElseThrow().getId());
        return cartProducts;
    }

    public Optional<Cart> addCart(Member member) {
        log.info("member_id ={}",member.getId());

        Cart cart = new Cart();
        cart.setMember(member);

        log.info("cart ={}", cart);
        cartRepository.save(cart);
        return Optional.of(cart);
    }

    public void addCartProduct(Member member, long itemId) {
        log.info("member.getuserlogin={}", member.getUserLoginId());
        Cart findcart = cartRepository.findByMemberId(member.getId()).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        Optional<CartProduct> existingCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart.getId(), findproduct.getId());

        if (existingCartProduct.isEmpty()) {
            CartProduct cartProduct = new CartProduct();

            cartProduct.setCart(findcart);
            cartProduct.setProduct(findproduct);
//            cartProduct.setCartId(findcart.getId());
//            cartProduct.setProductId(itemId);
            cartProduct.setQuantity(1); // 추후에 값을 받아와서 해야함.
            cartProduct.setTotalPrice(findproduct.getPrice());
            cartProductRepository.save(cartProduct);
        }
    }

    public void editUpQuantityCartProduct(Member member, long itemId) {
        Long memberId = memberRepository.findFirstByUserLoginId(member.getUserLoginId()).orElseThrow().getId();

        Cart findcart = cartRepository.findByMemberId(memberId).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        CartProduct findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart.getId(), findproduct.getId()).orElseThrow();
        findCartProduct.setQuantity(findCartProduct.getQuantity() + 1);
    }

    public void editDownQuantityCartProduct(Member member, long itemId) {
        Long memberId = memberRepository.findFirstByUserLoginId(member.getUserLoginId()).orElseThrow().getId();

        Cart findcart = cartRepository.findByMemberId(memberId).orElseThrow();
        Product findproduct = productRepository.findById(itemId).orElseThrow();

        CartProduct findCartProduct = cartProductRepository.findFirstByCartIdAndProductId(findcart.getId(), findproduct.getId()).orElseThrow();
        if (findCartProduct.getQuantity() > 0) {
            findCartProduct.setQuantity(findCartProduct.getQuantity() - 1);
        }

    }



    public void deleteCartProduct(Member member, long itemId) {
        Long memberId = memberRepository.findFirstByUserLoginId(member.getUserLoginId()).orElseThrow().getId();

        Cart findcart = cartRepository.findByMemberId(memberId).orElseThrow();
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
            cartProductForm.setImage(findProduct.getImage());
            cartProductForm.setName(findProduct.getName());
            cartProductForm.setNeededQuantity(cartProduct.getQuantity());
            cartProductForm.setTotalPrice(cartProduct.getTotalPrice());
            cartProductForms.add(cartProductForm);
        }

        return cartProductForms;
    }
}
