package project.studycafe.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.studycafe.domain.member.Member;
import project.studycafe.repository.member.JpaMemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpringDataJpaLoginService implements LoginService {
    private final JpaMemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findFirstByUserLoginId(loginId)
                .filter(m -> m.getUserPassword().equals(password))
                .orElse(null);

    }
}

