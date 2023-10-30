package project.studycafe.app.domain.product;

import lombok.Builder;
import lombok.Getter;
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

    //private로 하면 타임리프에서 에러가 남.
    //NoSuchMethod
    //Caused by: org.hibernate.HibernateException: HHH000143:
    protected Product() {
    }

    public static Product createEmptyProduct() {
        return new Product();
    }
    @Builder(builderMethodName = "easyBuilder", buildMethodName = "buildEasyProduct")
    public Product(String name, String category, int price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", readCount=" + readCount +
                ", likeCount=" + likeCount +
                '}';
    }
}
