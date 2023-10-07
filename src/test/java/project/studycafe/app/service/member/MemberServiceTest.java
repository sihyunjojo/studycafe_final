package project.studycafe.app.service.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.controller.form.member.OauthMemberForm;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.repository.member.JpaMemberRepository;
import project.studycafe.helper.exception.member.NotFoundMemberException;


import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@Transactional
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 순서 지정
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    JpaMemberRepository memberRepository;

    Long commonMemberId;
    Long oauthMemberId;
    Member commonMember;
    Member oauthMember;
    
    CommonMemberForm commonMemberForm = new CommonMemberForm("id123", "qw123", "회원", "닉네임",
            "남", "010-1234-5678", "서울", "강남로 1", "12345",
            "google@google.com", "2000-01-01");

    OauthMemberForm oauthMemberForm = new OauthMemberForm("naver@naver.com", "naver",
            "id12", "pw12", "네이버회원", "네이버닉네임", "여", "010-0000-0000",
            "경북", "구미", "00000", "1999-09-22");

    @BeforeEach
    void beforeEach(){
        log.info("start BeforeEach");
        
        commonMemberId = memberService.join(commonMemberForm);
        commonMember = memberService.findById(commonMemberId).orElseThrow(() -> {
            throw new NotFoundMemberException();
        });
        
    }


    @Test
    @DisplayName("초기화")
    @Order(1)
    void hm() {
    }

    @Test
    @DisplayName("회원가입 테스트")
    void join() {
        //given

        //then
        assertThat(commonMemberId).isNotNull();
        assertThat(commonMember.getUserLoginId()).isEqualTo("id123");
        assertThat(commonMember.getUserPassword()).isEqualTo("qw123");
        assertThat(commonMember.getName()).isEqualTo("회원");
        assertThat(commonMember.getNickname()).isEqualTo("닉네임");
        assertThat(commonMember.getGender()).isEqualTo("남");
        assertThat(commonMember.getPhone()).isEqualTo("010-1234-5678");
        assertThat(commonMember.getAddress().getCity()).isEqualTo("서울");
        assertThat(commonMember.getAddress().getStreet()).isEqualTo("강남로 1");
        assertThat(commonMember.getAddress().getZipcode()).isEqualTo("12345");
        assertThat(commonMember.getEmail()).isEqualTo("google@google.com");
        assertThat(commonMember.getBirth()).isEqualTo("2000-01-01");
    }

    @Test
    @DisplayName("일반회원 수정 테스트")
    void update_commonMember() {
        //when
        CommonMemberForm updateMemberForm = new CommonMemberForm("id123", "pw111", "회원11", "닉네임11",
                "여", "010-1111-1111", "서울", "강남로 1", "11111",
                "google11@google.com", "2000-11-11");
        //given
        memberService.update(commonMemberId, updateMemberForm);

        //then
        assertThat(commonMemberId).isNotNull();
        assertThat(commonMember.getUserLoginId()).isEqualTo("id123"); // 그대로
        assertThat(commonMember.getUserPassword()).isEqualTo("pw111");//
        assertThat(commonMember.getName()).isEqualTo("회원11");
        assertThat(commonMember.getNickname()).isEqualTo("닉네임11");
        assertThat(commonMember.getGender()).isEqualTo("여");
        assertThat(commonMember.getPhone()).isEqualTo("010-1111-1111");
        assertThat(commonMember.getAddress().getCity()).isEqualTo("서울");
        assertThat(commonMember.getAddress().getStreet()).isEqualTo("강남로 1");
        assertThat(commonMember.getAddress().getZipcode()).isEqualTo("11111");
        assertThat(commonMember.getEmail()).isEqualTo("google11@google.com");
        assertThat(commonMember.getBirth()).isEqualTo("2000-11-11");
    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    void deleteMember() {
        //given
        memberService.deleteMember(commonMember);

        //then
        assertThat(memberService.findById(commonMemberId)).isEmpty();
    }

    @Test
    @DisplayName("회원 고유 번호를 통한 멤버 찾기")
    void findById() {
        //given
        Member findMember = memberService.findById(commonMemberId).orElseThrow(()->{
            throw new NotFoundMemberException();
        });

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getUserLoginId()).isEqualTo("id123");
        assertThat(findMember.getUserPassword()).isEqualTo("qw123");
        assertThat(findMember.getName()).isEqualTo("회원");
        assertThat(findMember.getNickname()).isEqualTo("닉네임");
        assertThat(findMember.getEmail()).isEqualTo("google@google.com");
        assertThat(findMember.getBirth()).isEqualTo("2000-01-01");
    }

    @Test
    @DisplayName("로그인 아이디를 통한 멤버 찾기")
    void findByUserId() {
        //then
        Member findMember = memberService.findByUserId("id123").orElseThrow(()->{
            throw new NotFoundMemberException();
        });

        assertThat(findMember).isEqualTo(commonMember);
    }

    @Test
    @DisplayName("일반멤버의 이름과 핸드폰번호를 통한 멤버 찾기 테스트")
    void findMemberByNameAndPhone() {
        Member findMember = memberService.findMemberByNameAndPhone("회원", "010-1234-5678").orElseThrow(() -> {
            throw new NotFoundMemberException();
        });

        assertThat(findMember).isEqualTo(commonMember);

    }

    @Test
    @DisplayName("이메일과 제공자를 통해서 추후 인증멤버의 테스트도 준비하기")
    void findByEmailAndProvider() {
//        Member findMember = memberService.findMemberByEmailAndProvider("google@google.com", "google").orElseThrow(() -> {
//            throw new NotFoundMemberException();
//        });

//        assertThat(findMember).isEqualTo(oauthMember);
    }

    @Test
    void validateDuplicatedMemberLoginId() {
        //given
        boolean isId = memberService.validateDuplicatedMemberLoginId("id123");
        boolean isNotId = memberService.validateDuplicatedMemberLoginId("aaa");

        //then
        assertThat(isId).isTrue();
        assertThat(isNotId).isFalse();
    }

    @Test
    void testValidateDuplicatedMemberLoginId() {
        //when
        CommonMemberForm testCommonMemberForm = new CommonMemberForm("testId", "qw123", "test회원", "test닉네임",
                "남", "010-1234-5678", "서울", "강남로 1", "12345",
                "google@google.com", "2000-01-01");
        memberService.join(testCommonMemberForm);


        //given
        boolean isDuplicatedMemberLoginId = memberService.validateDuplicatedMemberLoginId("testId", commonMemberId);
        boolean isNotDuplicatedMemberLoginId = memberService.validateDuplicatedMemberLoginId("newId", commonMemberId);

        //then
        assertThat(isDuplicatedMemberLoginId).isTrue();
        assertThat(isNotDuplicatedMemberLoginId).isFalse();

    }

    @Test
    void validateDuplicatedMemberNickname() {
        //given
        boolean isNickname = memberService.validateDuplicatedMemberNickname(commonMemberForm.getNickname());
        boolean isNotNickname = memberService.validateDuplicatedMemberNickname("000");

        //then
        assertThat(isNickname).isTrue();
        assertThat(isNotNickname).isFalse();
    }

    @Test
    void testValidateDuplicatedMemberNickname() {
        //when
        CommonMemberForm testCommonMemberForm = new CommonMemberForm("testId", "qw123", "test회원", "test닉네임",
                "남", "010-1234-5678", "서울", "강남로 1", "12345",
                "google@google.com", "2000-01-01");
        Long testMemberId = memberService.join(testCommonMemberForm);


        //given
        boolean isDuplicatedMemberNickname = memberService.validateDuplicatedMemberNickname("test닉네임", commonMemberId); //닉네임
        boolean isNotDuplicatedMemberNickname = memberService.validateDuplicatedMemberNickname("new닉네임", commonMemberId);

        //then
        log.info("testId = {}, {}", testMemberId, commonMemberId);
        assertThat(isDuplicatedMemberNickname).isTrue();
        assertThat(isNotDuplicatedMemberNickname).isFalse();
    }


    @Test
    @DisplayName("멤버 객체 폼 객체로 변환 테스트")
    void memberToCommonMemberForm() {
        CommonMemberForm makeCommonMemberForm = memberService.memberToCommonMemberForm(commonMember);

        assertThat(makeCommonMemberForm).isEqualTo(commonMemberForm);
    }

    @Test
    void memberToOauthMemberForm() {
    }
}