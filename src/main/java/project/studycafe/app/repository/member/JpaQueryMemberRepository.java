package project.studycafe.app.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

public class JpaQueryMemberRepository{
    private final JPAQueryFactory query;
    public JpaQueryMemberRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }
}
