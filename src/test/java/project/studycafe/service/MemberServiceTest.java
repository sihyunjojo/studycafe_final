package project.studycafe.service;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.form.member.CommonMemberForm;
import project.studycafe.domain.member.Member;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.service.member.MemberService;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired JpaMemberRepository memberRepository;
    @Autowired MemberService memberService;


    @BeforeEach
    public void setUp() {
        // Mocking any call to save method in the memberRepository
        when(memberRepository.save(any(Member.class))).thenReturn(new Member());
    }

    @Test
    @DisplayName("회원가입")
    public void testJoin() {
        // Create a sample CommonMemberForm
        CommonMemberForm form = new CommonMemberForm();
        form.setUserLoginId("john.doe");
        form.setUserPassword("password123");
        form.setName("John Doe");
        form.setNickname("johndoe");
        form.setPhone("123-456-7890");
        form.setEmail("john.doe@example.com");
        form.setGender("Male");
        form.setBirth("1999-09-22");
        form.setCity("New York");
        form.setStreet("123 Main St");
        form.setZipcode("10001");

        // Call the join method
        Long memberId = memberService.join(form);

        // Assert that the memberService called save method on memberRepository
        verify(memberRepository).save(any(Member.class));

        // Assert that the returned memberId is not null
        assertNotNull(memberId);
    }
}