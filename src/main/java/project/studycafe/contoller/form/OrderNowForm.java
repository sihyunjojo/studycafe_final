package project.studycafe.contoller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNowForm {

    private long memberId;
    private long productId;
    private int productCount;
    private int productAllPrice;

    //delivery - address
    private String city;
    private String street;
    private String zipcode;

}