package project.studycafe.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.studycafe.domain.Member;
import project.studycafe.repository.member.JpaMemberRepository;

@Service
@RequiredArgsConstructor
public class JpaLoginService implements LoginService {
    private final JpaMemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findFirstByUserLoginId(loginId)
                .filter(m -> m.getUserPassword().equals(password))
                .orElse(null);
    }
}

