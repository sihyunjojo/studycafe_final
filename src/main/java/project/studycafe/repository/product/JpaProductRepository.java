package project.studycafe.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.Product;

import java.util.List;

public interface JpaProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByUpdatedTimeDesc();
}
