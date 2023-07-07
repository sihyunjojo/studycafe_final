package project.studycafe.service.login;

import project.studycafe.domain.Member;

public interface LoginService {
    public Member login(String loginId, String password);
}
