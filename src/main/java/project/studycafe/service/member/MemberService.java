package project.studycafe.service.member;

import project.studycafe.contoller.form.MemberCreateForm;
import project.studycafe.domain.Member;
import project.studycafe.contoller.form.MemberUpdateForm;

import java.util.Optional;

public interface MemberService {

    public Object join(MemberCreateForm form);
    public Optional<Member> findById(Member member);
    public Optional<Member> findByUserId(Member member);
    public Optional<Member> findMemberByNameAndPhone(Member member);
    public Optional<Member> update(long memberId, MemberUpdateForm updateForm);

    Boolean validateDuplicatedMemberLoginId(String memberLoginId);
}

