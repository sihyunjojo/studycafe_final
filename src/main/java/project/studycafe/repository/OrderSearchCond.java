package project.studycafe.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.studycafe.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
//@AllArgsConstructor
public class OrderSearchCond {

    private String memberNickname;
    private String productName;
    private String productCategory;

    private String minCreatedTime;
    private String maxCreatedTime;
    private OrderStatus orderStatus;
    private Integer perPageNum;

    private String sort;
    private String sortDirection;

    public OrderSearchCond(String memberNickname, String productName, String productCategory, String minCreatedTime, String maxCreatedTime, OrderStatus orderStatus, Integer perPageNum, String sort, String sortDirection) {
        this.memberNickname = memberNickname;
        this.productName = productName;
        this.productCategory = productCategory;
        this.minCreatedTime = minCreatedTime;
        this.maxCreatedTime = maxCreatedTime;
        this.orderStatus = orderStatus;
        this.perPageNum = perPageNum;
        if (perPageNum == null) {
            this.perPageNum = 10;
        }
        this.sort = sort;
        this.sortDirection = sortDirection;
    }
}
