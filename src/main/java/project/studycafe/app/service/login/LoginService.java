package project.studycafe.app.service.login;


import project.studycafe.app.domain.member.Member;

public interface LoginService {
    public Member login(String loginId, String password);
}
