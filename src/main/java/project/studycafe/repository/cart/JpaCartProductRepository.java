package project.studycafe.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.CartProduct;

import java.util.List;
import java.util.Optional;

public interface JpaCartProductRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findAllByCartId(Long id);
    Optional<CartProduct> findFirstByCartIdAndProductId(Long cartId, Long productId);
}
