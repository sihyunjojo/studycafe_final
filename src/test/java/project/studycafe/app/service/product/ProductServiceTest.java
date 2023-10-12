package project.studycafe.app.service.product;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.domain.product.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired ProductService productService;

    Product product;
    Long productId;

    @BeforeEach
    void setting() {
        product = new Product("초코파이", "과자", 700, 100);
        productService.addProduct(product);
        productId = productService.findProducts().get(0).getId();

    }

    // service에서 하는게 없어서 이건 controller테스트에서 꼼꼼히 해봐야할듯..?
    @Test
    @DisplayName("제품 추가 테스트")
    public void addProduct() {
        //when
        Product product = new Product("초코파이", "과자", 700, 100);
        //given
        productService.addProduct(product);

        //then
        List<Product> products = productService.findProducts();
        Product findProduct = products.get(0);
        log.info("findProduct = {}", findProduct);

        assertThat(findProduct.getName()).isEqualTo(product.getName());
        assertThat(findProduct.getCategory()).isEqualTo(product.getCategory());
        assertThat(findProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(findProduct.getQuantity()).isEqualTo(product.getQuantity());
    }

    // 추후 수량을 더하기 빼기로도 업데이트 할 수 있게 하면 좋겟다.
    @Test
    @DisplayName("제품 업데이트 테스트")
    public void updateProduct() {
        //when
        Product updateProduct = new Product("초코파이", "과자", 1000, 500);

        //given
        productService.updateProduct(productId, updateProduct);

        //then
        assertThat(product.getPrice()).isEqualTo(updateProduct.getPrice());
        assertThat(product.getQuantity()).isEqualTo(updateProduct.getQuantity());
    }

    @Test
    @DisplayName("제품 수량 업데이트 테스트")
    public void updateProduct_Quantity() {
        //when
        Product updateProduct = new Product("초코파이", "과자", 700, 500);

        //given
        productService.updateProduct(productId, updateProduct);

        //then
        assertThat(product.getQuantity()).isEqualTo(updateProduct.getQuantity());
    }
}