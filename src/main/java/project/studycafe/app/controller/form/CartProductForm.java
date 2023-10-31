package project.studycafe.app.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

@Data
@ToString

public class CartProductForm {

    private Long id;
    private boolean purchasedCheck;
    private String image;
    private String name;
    private Integer count;

    private Integer totalPrice;
}
