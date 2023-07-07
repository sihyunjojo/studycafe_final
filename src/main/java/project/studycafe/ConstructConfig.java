package project.studycafe;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.studycafe.repository.board.board.JpaQueryBoardRepository;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.repository.member.JpaQueryMemberRepository;
import project.studycafe.repository.product.JpaQueryProductRepository;
import project.studycafe.service.login.LoginService;
import project.studycafe.service.login.SpringDataJpaLoginService;
import project.studycafe.service.member.MemberService;
import project.studycafe.service.member.SpringDataJpaMemberService;

import javax.persistence.EntityManager;

@Configuration
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
}
