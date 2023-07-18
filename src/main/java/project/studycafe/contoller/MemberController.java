package project.studycafe.contoller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.studycafe.contoller.form.MemberForm;
import project.studycafe.domain.AttachmentFile;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.domain.Member;
import project.studycafe.contoller.form.MemberForm;
import project.studycafe.service.login.LoginService;
import project.studycafe.service.member.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final LoginService loginService;

    @GetMapping("/new")
    public String JoinForm(Model model) {
        model.addAttribute("member", new MemberForm());
        return "member/addMemberForm";
    }

    @PostMapping("/new")
    public String Join(@Validated @ModelAttribute("member") MemberForm form, BindingResult bindingResult, Model model) {
        if (memberService.validateDuplicatedMemberLoginId(form.getUserLoginId())) {
            bindingResult.reject("usedUserId", "이미 사용중인 아이디입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("회원가입 실패");
            log.info("errors = {}", bindingResult);
            model.addAttribute("member", new MemberForm());
            return "member/addMemberForm";
        }

        Object userId = memberService.join(form);
        log.info("userId = {}", userId);

        return "redirect:/";
    }

    @GetMapping("/edit")
    public String EditForm(@Login Member loginMember, Model model) {
        MemberForm MemberForm = memberService.memberToMemberForm(loginMember);
        model.addAttribute(LOGIN_MEMBER, MemberForm);
        
        return "member/editMemberForm";
    }

    //updateform의 필드들이 어자피 login
    @PostMapping("/edit")
    public String Edit(@Login Member loginMember, @Validated @ModelAttribute("member") MemberForm updateForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws NotFoundException {
        log.info("updateForm = {}", updateForm);

        if (bindingResult.hasErrors()) {
            log.info("bindingResult ={}", bindingResult);
            log.info("수정 실패");
            MemberForm MemberForm = memberService.memberToMemberForm(loginMember);
            model.addAttribute(LOGIN_MEMBER, MemberForm);
            return "member/editMemberForm";
        }

        Member updatedMember = memberService.update(loginMember.getId(), updateForm).orElseThrow();

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, updatedMember);

        return "redirect:/member/info";
    }

    @GetMapping("/delete")
    public String delete(@Login Member loginMember,HttpServletRequest request) {
//        memberService.removeForeignKeyMember(loginMember);
        memberService.deleteMember(loginMember);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/idquiry")
    public String findIdForm() {
        return "/member/findIdForm";
    }

    @PostMapping("/idquiry")
    public String findUserId(Member member, Model model) {
        Optional<Member> result = memberService.findMemberByNameAndPhone(member);
        model.addAttribute("member", result);

        return "member/findIdResult";
        // 예외처리할때, 일치하는 회원 없으면 없다고 보여주기
    }

    @GetMapping("/pwquiry")
    public String findPasswordForm() {
        return "/member/findPasswordForm";
    }

    @PostMapping("/pwquiry")
    public String findPassword(Member member, Model model) {
        Optional<Member> result = memberService.findByUserId(member);
        model.addAttribute("member", result);

        return "member/findPasswordResult";
    }

    @GetMapping("/info")
    public String InfoForm(@Login Member loginMember, Model model) {
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/member/info/";
        }
        log.info("member={}", loginMember);
        MemberForm memberForm = memberService.memberToMemberForm(loginMember);
        model.addAttribute(LOGIN_MEMBER, memberForm);

        return "member/memberInfo";
    }

    // ajax로 다시만들ㅡ
//    @PostMapping("/checkPw")
//    public String CheckPw(@Login Member loginMember, MemberForm updateForm, Model model) {
//        if (updateForm.getCheckPassword().equals(updateForm.getUserPassword())) {
//            model.addAttribute("same_password", "비밀번호 일치");
//            model.addAttribute("updateMember", updateForm);
//        } else {
//            model.addAttribute("different_password", "비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
//        }
//
//        return "member/editMemberForm";
//    }


}
