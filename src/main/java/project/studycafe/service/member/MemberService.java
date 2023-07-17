package project.studycafe.service.member;

import project.studycafe.contoller.form.MemberForm;
import project.studycafe.domain.Member;
import project.studycafe.contoller.form.MemberForm;

import java.util.Optional;

public interface MemberService {

    public Object join(MemberForm form);
    public Optional<Member> update(long memberId, MemberForm updateForm);
    public void deleteMember(Member member);
    public Optional<Member> findById(Member member);
    public Optional<Member> findByUserId(Member member);
    public Optional<Member> findMemberByNameAndPhone(Member member);
    Boolean validateDuplicatedMemberLoginId(String memberLoginId);
    public MemberForm memberToMemberForm(Member member);
}

