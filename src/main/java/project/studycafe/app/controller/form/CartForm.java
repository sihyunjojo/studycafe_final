package project.studycafe.app.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartForm {

    private long id;
    private long memberId;
    private List<CartProductForm> cartProductList = new ArrayList<>();


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CartForm{");
        sb.append("id=").append(id);
        sb.append(", memberId=").append(memberId);
        sb.append(", cartProductListSize=").append(cartProductList.size());
        sb.append(", cartProductList=[");

        for (int i = 0; i < cartProductList.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cartProductList.get(i).toString());
        }
        sb.append("]}");
        return sb.toString();
    }
}

