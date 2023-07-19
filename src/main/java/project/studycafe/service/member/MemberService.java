package project.studycafe.service.member;

import javassist.NotFoundException;
import project.studycafe.contoller.form.CommonMemberForm;
import project.studycafe.contoller.form.MemberForm;
import project.studycafe.contoller.form.OauthMemberForm;
import project.studycafe.domain.Member;

import java.util.Optional;

public interface MemberService {

    public Object join(CommonMemberForm form);
    public Optional<Member> update(long memberId, MemberForm form) throws NotFoundException;
//    public Optional<Member> oauthMemberUpdate(long memberId, MemberForm form) throws NotFoundException;

    public void deleteMember(Member member);

    public void removeForeignKeyMember(Member member);
    public Optional<Member> findById(Member member);
    public Optional<Member> findByUserId(Member member);
    public Optional<Member> findMemberByNameAndPhone(Member member);
    Boolean validateDuplicatedMemberLoginId(String memberLoginId);
    public CommonMemberForm memberToMemberForm(Member member);

    OauthMemberForm memberToOauthMemberForm(Member member);
}

