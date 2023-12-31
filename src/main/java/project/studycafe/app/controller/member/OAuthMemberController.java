package project.studycafe.app.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.member.SpringDataJpaMemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

import static project.studycafe.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthMemberController {

    private final SpringDataJpaMemberService memberService;

    @GetMapping("/success")
    public String login(Authentication authentication,
                        @RequestParam(required = false, defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {

        log.info("oauth login Success");
        // oAuth2User.toString() 예시 :
        // Name: [2346930276], Granted Authorities: [[USER]],
        // User Attributes: [{id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}]
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Optional<Member> loginMember = memberService.findMemberByEmailAndProvider(attributes.get("email").toString(), attributes.get("provider").toString());

        if (loginMember.isEmpty()) {
            // 찾을 수 없습니다.
            return "redirect:/";
        }

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, loginMember.orElseThrow());

        if (loginMember.orElseThrow().getNickname() == null) {
            return "redirect:/member/edit";
        }

        log.info("redirect = {}", redirectURL);

        return "redirect:" + redirectURL;
    }

}
