package project.studycafe.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderSearchCond {

    private String memberName;
    private String productName;
    private String category;
    private LocalDateTime minCreatedTime;
    private LocalDateTime maxCreatedTime;
    private int perPageNum;
    private String orderStatus;

    private String sort;

}
