package project.studycafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
