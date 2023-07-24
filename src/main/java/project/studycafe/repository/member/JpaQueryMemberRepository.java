package project.studycafe.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.studycafe.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static project.studycafe.domain.QMember.member;

@Slf4j
public class JpaQueryMemberRepository{
    private final JPAQueryFactory query;
    public JpaQueryMemberRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }
}
