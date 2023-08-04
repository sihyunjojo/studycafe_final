package project.studycafe.domain.form.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNowForm {

    private long id;
    private long memberId;

    private long productId;
    private int productCount;
    private int productAllPrice;

    private int orderTotalPrice;

}
