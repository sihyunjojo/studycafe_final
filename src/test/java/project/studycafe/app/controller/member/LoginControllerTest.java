package project.studycafe.app.controller.member;

import groovy.util.logging.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.studycafe.app.controller.form.member.CommonMemberForm;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class) // 테스트를 진행할 때 Junit에 내장된 샐행자 외에 다른 실행자를 실행(여기서는 SpringRunner) -> 부트 테스트와 Junit사이의 연결자 역할
//@WebMvcTest(controllers = LoginController.class) // Web에 집중할 수 있는 어노테이션 @Controller관련 어노테이션만 사용 가능해짐. JPA 기능이 작동하지 않음.
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc; // 웹 api 를 테스트 할때 사용합니다. get,post 등에 대한 테스트 가능.


    @Test
    @DisplayName("로그인 폼으로 get 테스트")
    public void loginForm() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login/loginForm"))
                .andExpect(model().attributeExists("loginForm"));
    }
}