package project.studycafe.app.controller.form.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import project.studycafe.app.controller.form.OrderItemForm;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm {

    private long id;
    private long memberId;

    private List<OrderItemForm> orderItems = new ArrayList<>();

    @NumberFormat(pattern = "###,###")
    private int orderTotalPrice;

    //delivery - address
    private String city;
    private String street;
    private String zipcode;

    private String orderStatus;
    private String deliveryStatus;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrderForm{");
        sb.append("id=").append(id);
        sb.append(", memberId=").append(memberId);
        sb.append(", orderItems=[");

        for (int i = 0; i < orderItems.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(orderItems.get(i).toString());
        }

        sb.append("]");
        sb.append(", orderTotalPrice=").append(orderTotalPrice);
        sb.append(", city='").append(city).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append(", zipcode='").append(zipcode).append('\'');
        sb.append(", orderStatus='").append(orderStatus).append('\'');
        sb.append(", deliveryStatus='").append(deliveryStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
