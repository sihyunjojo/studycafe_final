package project.studycafe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import project.studycafe.app.domain.board.Board;
import project.studycafe.helper.aop.logging.trace.LogTraceAspect;
import project.studycafe.app.repository.board.board.JpaQueryBoardRepository;
import project.studycafe.app.repository.member.JpaMemberRepository;
import project.studycafe.app.repository.member.JpaQueryMemberRepository;
import project.studycafe.app.repository.order.OrderQueryRepository;
import project.studycafe.app.repository.product.JpaQueryProductRepository;
import project.studycafe.app.service.login.LoginService;
import project.studycafe.app.service.login.SpringDataJpaLoginService;
import project.studycafe.app.service.member.MemberService;
import project.studycafe.app.service.member.SpringDataJpaMemberService;
import project.studycafe.helper.aop.logging.trace.LogTrace;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaAuditing // application에 있으면 springBoot
@RequiredArgsConstructor
public class ConstructConfig {

    private final EntityManager em;
    private final JpaMemberRepository jpaMemberRepository;

    @Bean
    public MemberService memberService(){
        return new SpringDataJpaMemberService(jpaMemberRepository);
    }

    @Bean
    public LoginService loginService() {
        return new SpringDataJpaLoginService(jpaMemberRepository);
    }

    @Bean
    public JpaQueryMemberRepository jpaQueryMemberRepository(){
        return new JpaQueryMemberRepository(em);
    }

    @Bean
    public JpaQueryBoardRepository jpaQueryBoardRepository(){
        return new JpaQueryBoardRepository(em);
    }

    @Bean
    public JpaQueryProductRepository jpaQueryProductRepository(){
        return new JpaQueryProductRepository(em);
    }

    @Bean
    public OrderQueryRepository orderQueryRepository(){
        return new OrderQueryRepository(em);
    }
}
