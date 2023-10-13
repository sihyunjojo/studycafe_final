package project.studycafe.app.service.dto.searchDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardSearchCond {
    private String title;
    private String userNickname;
    private String category;
    private String sort;
    private Integer perPageNum;
}
