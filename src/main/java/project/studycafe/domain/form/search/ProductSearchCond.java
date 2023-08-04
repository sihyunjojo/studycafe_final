package project.studycafe.domain.form.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
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
