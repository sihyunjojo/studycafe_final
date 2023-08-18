package project.studycafe.app.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.product.Product;

import java.util.List;

public interface JpaProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByUpdatedTimeDesc();
}
