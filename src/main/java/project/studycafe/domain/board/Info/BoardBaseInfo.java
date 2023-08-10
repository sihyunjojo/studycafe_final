package project.studycafe.domain.board.Info;

import lombok.ToString;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@ToString
@Embeddable
public class BoardBaseInfo {
    @NotEmpty
    private String title;
    @NotEmpty
    private String category; //추후 추상클래스로 만들어서 관리해야할지도
    @NotEmpty
    private String content;

    public static BoardBaseInfo createBoardInfo(String title, String category, String content) {
        BoardBaseInfo boardBaseInfo = new BoardBaseInfo();
        boardBaseInfo.setTitle(title);
        boardBaseInfo.setCategory(category);
        boardBaseInfo.setContent(content);
        return boardBaseInfo;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("category", category);
        map.put("content" ,content);
        return map;
    }


    private void setTitle(String title) {
        this.title = title;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    private void setContent(String content) {
        this.content = content;
    }
}
