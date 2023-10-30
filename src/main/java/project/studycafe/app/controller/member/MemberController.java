package project.studycafe.app.controller.member;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.controller.form.member.MemberForm;
import project.studycafe.app.controller.form.member.OauthMemberForm;
import project.studycafe.app.domain.member.Member;
import project.studycafe.helper.resolver.argumentresolver.Login;
import project.studycafe.app.service.member.MemberService;

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
        model.addAttribute("member", CommonMemberForm.createEmptyCommonMemberForm());
        return "member/addMemberForm";
    }

    @PostMapping("/new")
    public String Join(@Validated @ModelAttribute("member") CommonMemberForm form, BindingResult bindingResult) {
        if (memberService.validateDuplicatedMemberLoginId(form.getUserLoginId())) {
//            bindingResult.reject("usedUserLoginId", "이미 사용중인 아이디 입니다."); //글로벌에러
            bindingResult.rejectValue("userLoginId","unique.userLoginId","이미사용중인아이디입니다.");
        }

        if (memberService.validateDuplicatedMemberNickname(form.getNickname())) {
            bindingResult.rejectValue("nickname","unique.nickname");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "member/addMemberForm";
        }

        Long userId = memberService.join(form);
//        log.info("userId = {}", userId);

        return "redirect:/";
    }

    @GetMapping("/edit")
    public String EditForm(@Login Member loginMember, Model model) {
        if (loginMember.getProvider() != null) {
            OauthMemberForm oauthMemberForm = memberService.memberToOauthMemberForm(loginMember);
            model.addAttribute(LOGIN_MEMBER, oauthMemberForm);
            if (loginMember.getNickname() == null) {
                return "member/editOauthMemberNicknameForm";
            }
            return "member/editOauthMemberForm";
        }

        CommonMemberForm commonMemberForm = memberService.memberToCommonMemberForm(loginMember);
        model.addAttribute(LOGIN_MEMBER, commonMemberForm);

        return "member/editMemberForm";
    }


    //updateForm 의 필드들이 어자피 login
    @PostMapping("/edit")
    public String edit(@Login Member loginMember, @Validated @ModelAttribute("loginMember") MemberForm form, BindingResult bindingResult, HttpServletRequest request) throws NotFoundException {
        log.info("form ={}", form);
        if (memberService.validateDuplicatedMemberNickname(form.getNickname(), loginMember.getId())) {
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

    //updateForm 의 필드들이 어자피 login
    @PostMapping("/edit/oauth")
    public String editOauthMember(@Login Member loginMember, @Validated @ModelAttribute("loginMember") OauthMemberForm form, BindingResult bindingResult, HttpServletRequest request){
        if (memberService.validateDuplicatedMemberNickname(form.getNickname(), loginMember.getId())) {
            bindingResult.rejectValue("nickname","unique.nickname","이미 사용중인 닉네임입니다.");
        }

        if (form.getUserLoginId() != null) {
            if (memberService.validateDuplicatedMemberLoginId(form.getUserLoginId(), loginMember.getId())) {
                bindingResult.rejectValue("loginId", "unique.id", "이미 사용중인 아이디입니다.");
            }

            if (form.getUserPassword() == null){
                bindingResult.reject("loginBasicInfo", "아이디와 비밀번호는 함께 입력되어야 합니다.");
            }

        }

        if ( form.getUserLoginId() == null && form.getUserPassword() != null) {
            bindingResult.reject("loginBasicInfo", "아이디와 비밀번호는 함께 입력되어야 합니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("binding result = {}", bindingResult);
            return "member/editOauthMemberForm";
        }

        Member updatedMember = memberService.update(loginMember.getId(), form).orElseThrow();

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, updatedMember);

        return "redirect:/";
    }

    @PostMapping("/edit/oauth/nickname")
    public String editOauthMemberNickname(@Login Member loginMember, @Validated @ModelAttribute("loginMember") OauthMemberForm form, BindingResult bindingResult, HttpServletRequest request){
        if (memberService.validateDuplicatedMemberNickname(form.getNickname(), loginMember.getId())) {
            bindingResult.rejectValue("nickname","unique.nickname","이미 사용중인 닉네임입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("수정 실패");
            log.info("binding result = {}", bindingResult);
            return "member/editOauthMemberNicknameForm";
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
        Optional<Member> result = memberService.findMemberByNameAndPhone(member.getName(),member.getPhone());
        model.addAttribute("member", result);

        return "member/findIdResult";
    }

    @GetMapping("/find/pw")
    public String findPasswordForm() {
        return "/member/findPasswordForm";
    }

    @PostMapping("/find/pw")
    public String findPassword(Member member, Model model) {
        Optional<Member> result = memberService.findByUserId(member.getUserLoginId());
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

        CommonMemberForm commonMemberForm = memberService.memberToCommonMemberForm(loginMember);
        log.info("form = {}", commonMemberForm);
        model.addAttribute(LOGIN_MEMBER, commonMemberForm);

        return "member/memberInfo";
    }
}
