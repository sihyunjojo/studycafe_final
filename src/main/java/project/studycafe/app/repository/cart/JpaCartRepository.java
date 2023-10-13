package project.studycafe.app.repository.cart;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.Cart;
import project.studycafe.app.domain.member.Member;

import java.util.Optional;

public interface JpaCartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = "cart", type = EntityGraph.EntityGraphType.LOAD, value = "Cart.withMember")
    Optional<Cart> findFirstByMember(Member member);
}
