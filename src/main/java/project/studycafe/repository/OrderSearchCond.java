package project.studycafe.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderSearchCond {

    private String memberName;
    private String productName;
    private String productCategory;
    private LocalDateTime minCreatedTime;
    private LocalDateTime maxCreatedTime;
    private String orderStatus;
    private Integer perPageNum = 10;

    private String sort;
    private String sortDirection;
}
