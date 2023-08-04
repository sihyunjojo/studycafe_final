package project.studycafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.product.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
