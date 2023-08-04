package project.studycafe.service.login;


import project.studycafe.domain.member.Member;

public interface LoginService {
    public Member login(String loginId, String password);
}
