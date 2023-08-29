package project.studycafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.studycafe.app.service.OrderService;
import project.studycafe.app.service.board.BoardService;
import project.studycafe.app.service.board.CommentService;
import project.studycafe.app.service.board.ReplyService;
import project.studycafe.app.service.cart.CartService;
import project.studycafe.helper.aop.logging.trace.LogTrace;
import project.studycafe.helper.aop.logging.trace.LogTraceAspect;
import project.studycafe.helper.formatter.LocalDateTimeFormatter;
import project.studycafe.helper.interceptor.LoginCheckInterceptor;
import project.studycafe.helper.interceptor.PersonalAccessControlInterceptor;
import project.studycafe.helper.interceptor.PreAddressInterceptor;
import project.studycafe.helper.interceptor.SessionInterceptor;
import project.studycafe.helper.resolver.argumentresolver.LoginMemberArgumentResolver;

import java.util.List;


@Configuration
//BaseTimeEntity 클래스를 사용하는 패키지의 Configuration 클래스에서 @EnableJpaAuditing 어노테이션을 추가하여 Auditing 기능을 활성화해야 합니다.
@EnableJpaAuditing
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
                .excludePathPatterns(
                        "/", "/login", "/logout", "/oauth/**", "/download/**",
                        "/board", "/board/{boardId}", "board/add",
                        "/product", "/product/{productId}", "product/add",
                        "/comment/**", "/reply/**",
                        "/popup/**", "/css/**", "/*.ico", "/error", "/img/**", "/template/template/**",
                        "/member/new", "/member/find/**"
                );
        registry.addInterceptor(new SessionInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/popup/**", "/css/**", "/*.ico", "/error", "/img/**", "/template/template/**"
                );
        registry.addInterceptor(new PersonalAccessControlInterceptor())
                .order(3)
                .addPathPatterns(
                        "**/edit", "**/delete"
                )
                .excludePathPatterns(
                        "product/**/delete"
                );
        registry.addInterceptor(new PreAddressInterceptor())
                .order(4)
                .addPathPatterns("/**")
                .excludePathPatterns
                        (
                                "/popup/**", "/css/**", "/*.ico", "/error", "/img/**", "/template/template/**"
                        );
    }


    @Bean
    public LogTrace logTrace() {
        return new LogTrace();
    }

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }
}
