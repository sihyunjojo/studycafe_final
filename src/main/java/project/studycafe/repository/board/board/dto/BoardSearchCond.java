package project.studycafe.repository.board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class BoardSearchCond {
    private String title;
    private String userName;
    private String category;
    private String sort;
    private Integer perPageNum;

}
