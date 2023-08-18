package project.studycafe.app.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.member.Member;

import java.util.Optional;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findFirstByUserLoginId(String UserLoginId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findFirstByNameAndPhone(String name, String phone);

    Optional<Member> findByEmailAndProvider(String email, String provider);


}
