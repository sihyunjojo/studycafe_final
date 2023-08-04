package project.studycafe.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartProductForm {

    private Long productId;
    private boolean purchasedCheck;
    private Integer count;

    @NumberFormat(pattern = "###,###")
    private Integer totalPrice;
}
