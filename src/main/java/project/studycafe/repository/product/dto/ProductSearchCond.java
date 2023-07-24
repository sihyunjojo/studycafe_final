package project.studycafe.repository.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSearchCond {
    private String name;
    private String category;
    private Integer maxPrice;
    private Integer minPrice;
    private Integer minLikeCount;
    private String sort;
    private Integer perPageNum;

}
