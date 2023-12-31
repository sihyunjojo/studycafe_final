package project.studycafe.app.service.login;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.member.MemberService;
import project.studycafe.helper.exception.member.NotFoundMemberException;

import java.io.DataInput;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;
    @Autowired
    MemberService memberService;

    Member commonMember;
    Long commonMemberId;

    @BeforeEach
    public void setting() {
        CommonMemberForm commonMemberForm = new CommonMemberForm("id123", "pw123", "회원", "닉네임",
                "남", "010-1234-5678", "서울", "강남로 1", "12345",
                "google@google.com", "2000-01-01");
        commonMemberId = memberService.join(commonMemberForm);
        commonMember = memberService.findById(commonMemberId).orElseThrow(() -> {
            throw new NotFoundMemberException();
        });
    }

    @Test
    @DisplayName("일반 회원 로그인")
    public void commonMemberLogin() {
        //given

        //when
        Member loginMember = loginService.login("id123", "pw123");
        log.info("common ={}", commonMember);
        log.info("login = {}", loginMember);
        //then
        Assertions.assertThat(loginMember).isEqualTo(commonMember);
    }

    @Test
    @DisplayName("일반 회원 로그인 중 아이디가 달라서 실패")
    public void commonMemberLogin_IdException() {
        //when
        Member loginMember = loginService.login("id1234", "pw123");
        //then
        Assertions.assertThat(loginMember).isNull();
    }
    @Test
    @DisplayName("일반 회원 로그인 중 비밀번호가 달라서 실패")
    public void commonMemberLogin_PwException() {
        //when
        Member loginMember = loginService.login("id123", "pw1234");
        //then
        Assertions.assertThat(loginMember).isNull();
    }

    @Test
    @DisplayName("일반 회원 로그인 중 값이 입력되지 않아서 실패")
    public void commonMemberLogin_EmptyException() {
        //when
        Member loginMember = loginService.login("", "");
        //then
        Assertions.assertThat(loginMember).isNull();
    }


    // 이것을 제외하고는 contorller 테스트나 oauthService 테스트에서 해야할거 같음.
    @Test
    @DisplayName("Oauth 회원 로그인")
    public void oauthMemberLogin(){

    }

}