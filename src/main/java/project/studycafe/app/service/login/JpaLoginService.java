package project.studycafe.app.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.repository.member.JpaMemberRepository;

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

