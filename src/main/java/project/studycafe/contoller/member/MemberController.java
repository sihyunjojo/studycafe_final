package project.studycafe.contoller.member;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.studycafe.domain.form.member.CommonMemberForm;
import project.studycafe.domain.form.member.OauthMemberForm;
import project.studycafe.domain.member.Member;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.service.member.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/new")
    public String JoinForm(Model model) {
        model.addAttribute("member", new CommonMemberForm());
        return "member/addMemberForm";
    }

    @PostMapping("/new")
    public String Join(@Validated @ModelAttribute("member") CommonMemberForm form, BindingResult bindingResult, Model model) {
        if (memberService.validateDuplicatedMemberLoginId(form.getUserLoginId())) {
//            bindingResult.reject("usedUserLoginId", "이미 사용중인 아이디 입니다."); //글로벌에러
            bindingResult.rejectValue("userLoginId","unique.userLoginId","이미사용중인아이디입니다.");
        }

        if (memberService.validateDuplicatedMemberNickname(form)) {
            bindingResult.rejectValue("nickname","unique.nickname");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "member/addMemberForm";
        }

        Object userId = memberService.join(form);
        log.info("userId = {}", userId);

        return "redirect:/";
    }

    @GetMapping("/edit")
    public String EditForm(@Login Member loginMember, Model model) {
        if (loginMember.getProvider() != null) {
            OauthMemberForm oauthMemberForm = memberService.memberToOauthMemberForm(loginMember);
            model.addAttribute(LOGIN_MEMBER, oauthMemberForm);
            return "member/editOauthMemberForm";
        }

        CommonMemberForm commonMemberForm = memberService.memberToMemberForm(loginMember);
        model.addAttribute(LOGIN_MEMBER, commonMemberForm);

        return "member/editMemberForm";
    }


    //updateform의 필드들이 어자피 login
    @PostMapping("/edit")
    public String Edit(@Login Member loginMember, @Validated @ModelAttribute("loginMember") CommonMemberForm form, BindingResult bindingResult, Model model, HttpServletRequest request) throws NotFoundException {
        if (memberService.validateDuplicatedMemberNickname(form, loginMember.getId())) {
            bindingResult.rejectValue("nickname","unique.nickname","이미 사용중인 닉네임입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult ={}", bindingResult);
            return "member/editMemberForm";
        }

        Member updatedMember = memberService.update(loginMember.getId(), form).orElseThrow();

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, updatedMember);

        return "redirect:/member/info";
    }

    //updateform의 필드들이 어자피 login
    @PostMapping("/edit/oauth")
    public String Edit(@Login Member loginMember, @Validated @ModelAttribute("loginMember") OauthMemberForm form, BindingResult bindingResult, Model model, HttpServletRequest request) throws NotFoundException {
        if (memberService.validateDuplicatedMemberNickname(form, loginMember.getId())) {
            bindingResult.rejectValue("nickname","unique.nickname","이미 사용중인 닉네임입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("수정 실패");
            log.info("binding result = {}", bindingResult);
            return "member/editOauthMemberForm";
        }

        Member updatedMember = memberService.update(loginMember.getId(), form).orElseThrow();

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, updatedMember);

        return "redirect:/";
    }


    @GetMapping("/delete")
    public String delete(@Login Member loginMember,HttpServletRequest request) {
        memberService.deleteMember(loginMember);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/find/id")
    public String findIdForm() {
        return "/member/findIdForm";
    }

    @PostMapping("/find/id")
    public String findUserId(Member member, Model model) {
        Optional<Member> result = memberService.findMemberByNameAndPhone(member);
        model.addAttribute("member", result);

        return "member/findIdResult";
    }

    @GetMapping("/find/pw")
    public String findPasswordForm() {
        return "/member/findPasswordForm";
    }

    @PostMapping("/find/pw")
    public String findPassword(Member member, Model model) {
        Optional<Member> result = memberService.findByUserId(member);
        model.addAttribute("member", result);

        return "member/findPasswordResult";
    }

    @GetMapping("/info")
    public String InfoForm(@Login Member loginMember, Model model) {
        if (loginMember == null) {
            return "redirect:/login?redirectURL=/member/info";
        }

        if (loginMember.getProvider() != null) {
            log.info("provider = {}", loginMember.getProvider());
            OauthMemberForm oauthMemberForm = memberService.memberToOauthMemberForm(loginMember);
            model.addAttribute(LOGIN_MEMBER, oauthMemberForm);
            return "member/oauthMemberInfo";
        }

        log.info("member={}", loginMember);
        CommonMemberForm commonMemberForm = memberService.memberToMemberForm(loginMember);
        model.addAttribute(LOGIN_MEMBER, commonMemberForm);

        return "member/memberInfo";
    }
}
