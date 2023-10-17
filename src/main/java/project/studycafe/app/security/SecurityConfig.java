package project.studycafe.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import project.studycafe.app.domain.enums.MemberLevel;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.oauth.OAuthService;


// 어댑터는 호환되지 않는 두개의 인터페이스나 클래스를 연결하여 함께 작동하도록 만드는 역할을 한다.
// 스프링 시큐리티를 사용하면 /oauth2/authorization/{OAuth 서비스 이름} 형식으로 요청만 보내면 알아서 다 해결해줍니다.
@EnableWebSecurity //spring security 설정을 활성화시켜주는 어노테이션
@RequiredArgsConstructor //final 필드 생성자 만들어줌
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final OAuthService oAuthService;


    // OauthMember에 대해서만 적용이 되는거 같음. 그치그치 그게 맞는거 같아
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()//csrf 공격을 막아주는 옵션을 disalbe, rest api같은 경우에는 브라우저를 통해 request 받지 않기 때문에 해당 옵션을 꺼도 됩니다.
                .headers().frameOptions().disable()
                .and()
                    .authorizeHttpRequests()
                    .antMatchers("/", "/css/**", "/image/**", "/js/**").permitAll()
                    .antMatchers("/seat/**").hasRole(MemberLevel.USER.name())
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/template/access-denied.html") // 접근 거부 페이지 설정 status가 403일떄,
                .and()
                    .logout()   //기본 logout url = "/logout"
                     .logoutSuccessUrl("/") //logout 요청시 홈으로 이동
                .and()
                    .oauth2Login() //OAuth2 로그인 설정 시작점
                        .loginPage("/login")// oauth를 로그인하기 위해서 가야하는 주소폼을 입력해주는거임.
                // 이 부분은 사용자가 OAuth2 인증을 시작하기 위해 방문해야 하는 로그인 페이지의 경로를 지정합니다.
                // 일반적으로 사용자가 OAuth2 인증을 시작하려면 /login 경로로 이동해야 하며, 이 경로에 로그인 페이지가 제공될 것입니다.

                // 이 코드를 작성하니까 기존 @{/login}을 사용했을때 ouath 이상한 화면으로 넘어가던게 그냥 됬음.
                //// 로그인이 되어 있어도 Oauth으로 로그인이 안되어 있으니까 이 방법으로 로그인하라고 login으로 넘겨줌.
                //
                        .defaultSuccessUrl("/oauth/success", true) //OAuth2 성공시 redirect
                        .userInfoEndpoint() //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                            .userService(oAuthService); //OAuth2 로그인 성공 시, 작업을 진행할 oAuthService

    }
}