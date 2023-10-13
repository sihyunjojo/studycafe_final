package project.studycafe.app.controller.member;

import groovy.util.logging.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.studycafe.app.controller.form.member.CommonMemberForm;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class) // 테스트를 진행할 때 Junit에 내장된 샐행자 외에 다른 실행자를 실행(여기서는 SpringRunner) -> 부트 테스트와 Junit사이의 연결자 역할
class MemberControllerTest {


    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc; // 웹 api 를 테스트 할때 사용합니다. get,post 등에 대한 테스트 가능.

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void Join() throws Exception {
        // given
        // 이 코드도 builder 패턴이면 더 좋을듯
        CommonMemberForm form = new CommonMemberForm("id123", "pw123", "회원", "닉네임",
                "남", "010-1234-5678", "서울", "강남로 1", "12345",
                "google@google.com", "2000-01-01");

        String url = "http://localhost:" + port + "/member/new";

        //when
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, form, String.class);

        //then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(responseEntity.getBody())

        mvc.perform(post("member/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("/"));

    }
}