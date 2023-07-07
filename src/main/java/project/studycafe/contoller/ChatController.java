package project.studycafe.contoller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.studycafe.contoller.form.ChatMessage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@SessionAttributes("messages")
public class ChatController {

    @ModelAttribute("messages")
    public List<ChatMessage> messages() {
        return new ArrayList<>(); // 세션에 저장할 메시지 리스트 초기화
    }

    @GetMapping("/chat")
    public String chatForm(Model model) {
        model.addAttribute("newMessage", new ChatMessage()); // 새로운 메시지를 입력받을 폼을 위한 모델 속성 추가
        return "chat/chat"; // 채팅 화면을 나타내는 템플릿 이름 반환
    }

    @PostMapping("/chat/sendMessage")
    public String sendMessage(@ModelAttribute("newMessage") ChatMessage newMessage,
                              @ModelAttribute("messages") List<ChatMessage> messages) {
        messages.add(newMessage); // 메시지 리스트에 새로운 메시지 추가
        log.info("messages={}", messages);
        return "redirect:/chat"; // 채팅 화면으로 리다이렉트
    }
}

