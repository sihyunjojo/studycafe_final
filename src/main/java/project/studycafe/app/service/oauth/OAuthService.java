package project.studycafe.app.service.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.domain.enums.OAuthAttributes;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.domain.member.MemberProfile;
import project.studycafe.app.repository.member.JpaMemberRepository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/*
    OAuth2 로그인 성공시 DB에 저장하는 작업
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final JpaMemberRepository memberRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스(kakao, google, naver)에서 가져온 유저 정보를 담고있음

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId(); // OAuth 서비스 이름(ex. kakao, naver, google)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값
        // 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오는 기본 지원하지 않는다.
        // 네이버 구글 동시 지원할떄 사용됨.
        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth 서비스의 유저 정보들

        log.info("oAuth2User ={}", oAuth2User);
        log.info("registrationId ={}", registrationId);
        log.info("userNameAttributeName ={}", userNameAttributeName);
        log.info("attributes ={}", attributes);


        MemberProfile memberProfile = OAuthAttributes.extract(registrationId, attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        memberProfile.setProvider(registrationId);
        log.info("memberProfile ={}", memberProfile);

        Member member = saveOrUpdate(memberProfile);
        log.info("member = {}", member);

        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, memberProfile, registrationId);

        //OAuth2 인증을 통해 가져온 사용자 정보를 기반으로 Spring Security에서 사용할 수 있는 DefaultOAuth2User 객체를 생성하는 역할을 합니다. 이 객체는 인증 및 권한 부여를 위해 사용될 수 있습니다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                customAttribute,
                userNameAttributeName);
    }

    private Map<String, Object> customAttribute(Map<String, Object> attributes, String userNameAttributeName, MemberProfile memberProfile, String registrationId) {
        log.info("customAttribute");
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", memberProfile.getName());
        customAttribute.put("email", memberProfile.getEmail());
        return customAttribute;

    }

    private Member saveOrUpdate(MemberProfile memberProfile) {
        Member member = memberRepository.findByEmailAndProvider(memberProfile.getEmail(), memberProfile.getProvider())
                .map(m -> update(m, memberProfile.getName(),memberProfile.getEmail()))// OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 우리 DB에도 update
                .orElse(memberProfile.toMember());

        log.info("member ={}", member);
        return memberRepository.save(member);
    }

    private Member update(Member member, String name, String email) {
        member.setName(name);
        member.setEmail(email);
        if (member.getNickname() == null) { // 이전 닉네임과 새 닉네임이 다를 경우에만 업데이트
            member.setNickname(generateRandomNickname());
        }

        return member;
    }

    public String generateRandomNickname() {
        UUID uuid = UUID.randomUUID();
        String nickname = uuid.toString().replace("-", "");

        return nickname.substring(0, 10); // 예시로 10자리까지 잘라서 사용
    }


}