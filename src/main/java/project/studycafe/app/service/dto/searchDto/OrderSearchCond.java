package project.studycafe.app.service.dto.searchDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.studycafe.app.domain.enums.status.OrderStatus;

@Data
@AllArgsConstructor
public class OrderSearchCond {

    private String memberNickname;
    private String productName;
    private String productCategory;

    private String minCreatedTime;
    private String maxCreatedTime;
    private OrderStatus orderStatus;
    private String sort;
    private Integer perPageNum;

//    public OrderSearchCond(String memberNickname, String productName, String productCategory, String minCreatedTime, String maxCreatedTime, OrderStatus orderStatus, Integer perPageNum, String sort) {
//        this.memberNickname = memberNickname;
//        this.productName = productName;
//        this.productCategory = productCategory;
//        this.minCreatedTime = minCreatedTime;
//        this.maxCreatedTime = maxCreatedTime;
//        this.orderStatus = orderStatus;
//        this.perPageNum = perPageNum;
//        if (perPageNum == null) {
//            this.perPageNum = 10;
//        }
//        this.sort = sort;
//    }
}
