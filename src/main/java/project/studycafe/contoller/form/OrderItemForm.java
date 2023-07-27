package project.studycafe.contoller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemForm {
    private long id;
    private int count;
    private int allPrice;

    public boolean isEmpty() {
        return id == 0 && count == 0 && allPrice == 0;
    }
}
