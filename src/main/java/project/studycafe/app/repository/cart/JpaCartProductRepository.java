package project.studycafe.app.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.Cart;
import project.studycafe.app.domain.product.CartProduct;
import project.studycafe.app.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface JpaCartProductRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findAllByCartId(Long id);
    Optional<CartProduct> findFirstByCartAndProduct(Cart cart, Product product);
}
