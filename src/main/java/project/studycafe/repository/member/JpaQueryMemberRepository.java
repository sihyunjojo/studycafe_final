package project.studycafe.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

@Slf4j
public class JpaQueryMemberRepository{
    private final JPAQueryFactory query;
    public JpaQueryMemberRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }
}
