package project.studycafe.helper.resolver.argumentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.studycafe.SessionConst;
import project.studycafe.app.domain.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// 객체를 찾아서 제공하는 역할을한다.
// 주로 의존성 주입이나 객체 검색과 관련된 역할을 한다.
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {//@Login Member loginMember이였으니까
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class); //@Login인지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType()); //Member class인지 확인

        return hasLoginAnnotation && hasMemberType;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        log.info("resolverArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
