package project.studycafe.service.member;

import project.studycafe.domain.Member;
import project.studycafe.repository.member.dto.MemberUpdateForm;

import java.util.Optional;

public interface MemberService {

    public String join(Member member);
    public Optional<Member> findById(Member member);
    public Optional<Member> findByUserId(Member member);
    public Optional<Member> findMemberByNameAndPhone(Member member);
    public Optional<Member> update(long memberId, MemberUpdateForm updateForm);

}

