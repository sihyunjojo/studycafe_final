package project.studycafe.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.product.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
