package project.studycafe.contoller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemForm {

    //productId
    private long id;
    private int count;

    @NumberFormat(pattern = "###,###")
    private int allPrice;

    public boolean isEmpty() {
        return id == 0 && count == 0 && allPrice == 0;
    }
}
