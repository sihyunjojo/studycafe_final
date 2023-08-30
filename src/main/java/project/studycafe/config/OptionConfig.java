package project.studycafe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.studycafe.app.repository.DeliveryRepository;
import project.studycafe.app.repository.OrderItemRepository;
import project.studycafe.app.repository.board.board.JpaBoardRepository;
import project.studycafe.app.repository.board.board.JpaQueryBoardRepository;
import project.studycafe.app.repository.board.comment.JpaCommentRepository;
import project.studycafe.app.repository.board.reply.JpaReplyRepository;
import project.studycafe.app.repository.cart.JpaCartProductRepository;
import project.studycafe.app.repository.cart.JpaCartRepository;
import project.studycafe.app.repository.member.JpaMemberRepository;
import project.studycafe.app.repository.order.OrderQueryRepository;
import project.studycafe.app.repository.order.OrderRepository;
import project.studycafe.app.repository.product.JpaProductRepository;
import project.studycafe.app.service.OrderService;
import project.studycafe.app.service.board.BoardService;
import project.studycafe.app.service.board.CommentService;
import project.studycafe.app.service.board.ReplyService;
import project.studycafe.app.service.cart.CartService;
import project.studycafe.helper.aop.logging.trace.LogTrace;
import project.studycafe.helper.aop.logging.trace.LogTraceAspect;
import project.studycafe.helper.formatter.LocalDateTimeFormatter;
import project.studycafe.helper.interceptor.*;
import project.studycafe.helper.resolver.argumentresolver.LoginMemberArgumentResolver;

import java.util.List;


@Configuration
//BaseTimeEntity 클래스를 사용하는 패키지의 Configuration 클래스에서 @EnableJpaAuditing 어노테이션을 추가하여 Auditing 기능을 활성화해야 합니다.
@EnableJpaAuditing
@RequiredArgsConstructor
public class OptionConfig implements WebMvcConfigurer {

    private final JpaMemberRepository memberRepository;
    private final JpaBoardRepository boardRepository;
    private final JpaQueryBoardRepository boardQueryRepository;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final JpaProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;
    private final JpaCartRepository cartRepository;
    private final JpaCartProductRepository cartProductRepository;
    private final JpaCommentRepository commentRepository;
    private final JpaReplyRepository replyRepository;


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
        registry.addInterceptor(new LoginCheckInterceptor(new CommentService(commentRepository,memberRepository,boardRepository),new ReplyService(replyRepository,memberRepository,commentRepository)))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns
                        (
                                "/", "/login", "/logout", "/oauth/**", "/download/**",
                                "/board", "/board/{boardId}", "board/add",
                                "/product", "/product/{productId}", "product/add",
//                                "/comment/**", "/reply/**",
                                "/popup/**", "/css/**", "/*.ico", "/error", "/img/**", "/template/template/**",
                                "/member/new", "/member/find/**"
                        );
        registry.addInterceptor(new SessionInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns
                        (
                                "/popup/**", "/css/**", "/*.ico", "/error", "/img/**", "/template/template/**"
                        );
        registry.addInterceptor(new PreAddressInterceptor())
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns
                        (
                                "/popup/**", "/css/**", "/*.ico", "/error", "/img/**", "/template/template/**"
                        );
        registry.addInterceptor(new PersonalAccessControlInterceptor(new BoardService(boardRepository, boardQueryRepository, memberRepository), new OrderService(orderRepository, orderQueryRepository, memberRepository, productRepository, deliveryRepository, orderItemRepository), new CartService(cartRepository, cartProductRepository, productRepository), new CommentService(commentRepository, memberRepository, boardRepository), new ReplyService(replyRepository, memberRepository, commentRepository)))
                .order(4)
                .addPathPatterns(
                        "/board/*/edit", "/**/delete"
                )
                .excludePathPatterns(
                        "/product/**/delete"
                );
        registry.addInterceptor(new AccessControlByLevelInterceptor())
                .order(5)
                .addPathPatterns(
                        "/product/**/edit/**", "/product/**/delete"
                )
                .excludePathPatterns(

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
