package project.studycafe.contoller.form;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;

    // 생성자, getter, setter 생략
}