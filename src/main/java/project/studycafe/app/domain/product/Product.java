package project.studycafe.app.domain.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.helper.exception.order.NotEnoughStockException;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter @Setter
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;
    private String category;
    private Integer quantity; // 수량
    @NotNull
    private int price;

    private String description; //설명
    private String image; // 이미지 url
    private Integer readCount;
    private Integer likeCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    public void setting() {
        if (this.readCount == null && this.likeCount == null) {
            this.readCount = 0;
            this.likeCount = 0;
        }
    }

    //==비지니스 로직==//
    public void addStock(int quantity) {
        this.quantity += quantity;
    }

    public void removeStock(int quantity) {
        log.info("this = {},quantity = {}", this.quantity, quantity);
        int restStock = this.quantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.quantity = restStock;
    }

    public Product(Long id, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
