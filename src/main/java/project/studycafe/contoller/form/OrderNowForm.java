package project.studycafe.contoller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

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

    //delivery - address
    private String city;
    private String street;
    private String zipcode;

    public OrderNowForm(long id, long memberId, long productId, int productCount, String city, String street, String zipcode,int OrderTotalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.productCount = productCount;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.orderTotalPrice = OrderTotalPrice;
    }
}
