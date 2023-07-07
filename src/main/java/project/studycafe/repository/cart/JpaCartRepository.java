package project.studycafe.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.Cart;

import java.util.Optional;

public interface JpaCartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMemberId(Long memberId);

}