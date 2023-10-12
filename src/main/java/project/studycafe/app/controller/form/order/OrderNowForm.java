package project.studycafe.app.controller.form.order;

import lombok.NoArgsConstructor;


import lombok.Data;
@Data
@NoArgsConstructor
public class OrderNowForm {

    private long id;
    private long memberId;

    private long productId;
    private int productCount;
    private int productAllPrice;

    private int orderTotalPrice;

    public OrderNowForm(long memberId, long productId, int productCount) {
        this.memberId = memberId;
        this.productId = productId;
        this.productCount = productCount;
    }
}
