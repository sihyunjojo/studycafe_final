package project.studycafe;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.studycafe.resolver.argumentresolver.LoginMemberArgumentResolver;
import project.studycafe.formatter.LocalDateTimeFormatter;
import project.studycafe.interceptor.LoginCheckInterceptor;

import java.util.List;

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
                        ("/", "/login", "/logout",
                                "/board", "/board/{boardId}", "board/add",
                                "/product", "/product/{productId}","product/add",
                                "/comment/**", "/reply/**",
                                "/popup/**","/css/**", "/*.ico", "/error", "/img/**", "/template/template/**",
                                "/member/**")
                .addPathPatterns("/member/info");

    }

}
