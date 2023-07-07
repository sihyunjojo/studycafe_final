package project.studycafe.service.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardForm {
    private Long id;

    private String userName;
    private String title;
    private String category;
    private String content;
    private LocalDateTime createdTime;
    private String attachmentFile;
    private String popup;
    private Integer readCount;
    private Integer likeCount;


}
