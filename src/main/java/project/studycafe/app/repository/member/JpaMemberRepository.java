package project.studycafe.app.repository.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.member.Member;

import java.util.Optional;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "cart", type = EntityGraph.EntityGraphType.LOAD, value = "Member.withCart")
    Optional<Member> findById(Long Id);

    Optional<Member> findFirstByUserLoginId(String UserLoginId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findFirstByNameAndPhone(String name, String phone);

    Optional<Member> findByEmailAndProvider(String email, String provider);


}
