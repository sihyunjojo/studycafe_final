package project.studycafe.service.cart;

import lombok.Data;

@Data
public class CartProductForm {
    private boolean check;
    private Long productId;
    private String image;
    private String name;
    private Integer neededQuantity;
    private Integer totalPrice;

    public CartProductForm() {
    }

    public CartProductForm(boolean check, Long productId, String image, String name, Integer neededQuantity, Integer totalPrice) {
        this.check = check;
        this.productId = productId;
        this.image = image;
        this.name = name;
        this.neededQuantity = neededQuantity;
        this.totalPrice = totalPrice;
    }
}
