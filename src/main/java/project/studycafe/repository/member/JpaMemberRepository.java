package project.studycafe.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.Member;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findFirstByUserLoginId(String UserLoginId);
    Optional<Member> findByEmailAndProvider(String email, String provider);
    Optional<Member> findFirstByNameAndPhone(String name, String phone);
}
