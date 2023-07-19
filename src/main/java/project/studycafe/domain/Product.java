package project.studycafe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @PrePersist
    public void setting() {
        if (this.readCount == null && this.likeCount == null) {
            this.readCount = 0;
            this.likeCount = 0;
        }
    }
}
