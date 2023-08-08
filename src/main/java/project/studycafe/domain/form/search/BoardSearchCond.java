package project.studycafe.domain.form.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class BoardSearchCond {
    private String title;
    private String userNickname;
    private String category;
    private String sort;
    private Integer perPageNum;
}