package project.studycafe.app.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.Cart;

import java.util.Optional;

public interface JpaCartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findFirstByMemberId(Long memberId);

}
