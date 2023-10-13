package project.studycafe.app.service.oauth;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.controller.form.member.OauthMemberForm;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.login.LoginService;
import project.studycafe.app.service.member.MemberService;
import project.studycafe.helper.exception.member.NotFoundMemberException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class) // 테스트를 진행할 때 Junit에 내장된 샐행자 외에 다른 실행자를 실행(여기서는 SpringRunner) -> 부트 테스트와 Junit사이의 연결자 역할
class OAuthServiceTest {

    @Autowired
    LoginService loginService;
    @Autowired
    OAuthService oAuthService;

    Long oauthMemberId;
    Member oauthMember;

    OauthMemberForm oauthMemberForm = new OauthMemberForm("naver@naver.com", "naver",
            "id12", "pw12", "네이버회원", "네이버닉네임", "여", "010-0000-0000",
            "경북", "구미", "00000", "1999-09-22");

    @BeforeEach
    void setting() {
    }

    @Test
    void loadUser(){
        //when

        //given
        //then

    }

}