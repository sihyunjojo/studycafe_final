package project.studycafe;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.studycafe.interceptor.SessionInterceptor;
import project.studycafe.resolver.argumentresolver.LoginMemberArgumentResolver;
import project.studycafe.formatter.LocalDateTimeFormatter;
import project.studycafe.interceptor.LoginCheckInterceptor;

import javax.persistence.Entity;
import java.util.List;

//BaseTimeEntity 클래스를 사용하는 패키지의 Configuration 클래스에서 @EnableJpaAuditing 어노테이션을 추가하여 Auditing 기능을 활성화해야 합니다.
@EnableJpaAuditing
@Configuration
public class OptionConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateTimeFormatter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns
                        ("/", "/login", "/logout","/oauth/**","/download/**",
                                "/board", "/board/{boardId}", "board/add",
                                "/product", "/product/{productId}","product/add",
                                "/comment/**", "/reply/**",
                                "/popup/**","/css/**", "/*.ico", "/error", "/img/**", "/template/template/**",
                                "/member/**")
                .addPathPatterns("/member/info");

        registry.addInterceptor(new SessionInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns
                        (
//                                "/", "/login", "/logout", "/oauth/**",
//                                "/board", "/board/{boardId}", "board/add",
//                                "/product", "/product/{productId}", "product/add",
//                                "/member/**",
                                "/popup/**", "/css/**", "/*.ico", "/error", "/img/**", "/template/template/**"
                        );
    }

}
