package project.studycafe.contoller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.studycafe.contoller.form.CommonMemberForm;
import project.studycafe.contoller.form.OauthMemberForm;
import project.studycafe.resolver.argumentresolver.Login;
import project.studycafe.domain.Member;
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
//            model.addAttribute("member", new CommonMemberForm());
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
//            CommonMemberForm CommonMemberForm = memberService.memberToMemberForm(loginMember);
//            model.addAttribute(LOGIN_MEMBER, CommonMemberForm);
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
//            OauthMemberForm OauthMemberForm = memberService.memberToOauthMemberForm(loginMember);
//            model.addAttribute(LOGIN_MEMBER, OauthMemberForm);
            return "member/editOauthMemberForm";
        }

        Member updatedMember = memberService.update(loginMember.getId(), form).orElseThrow();

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, updatedMember);

        return "redirect:/member/info";
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
