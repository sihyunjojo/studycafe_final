package project.studycafe.service.member;

import javassist.NotFoundException;
import project.studycafe.domain.form.member.CommonMemberForm;
import project.studycafe.domain.form.member.MemberForm;
import project.studycafe.domain.form.member.OauthMemberForm;
import project.studycafe.domain.member.Member;

import java.util.Optional;

public interface MemberService {

    public Long join(CommonMemberForm form);
    public Optional<Member> update(long memberId, MemberForm form) throws NotFoundException;
    public void deleteMember(Member member);
//    public void removeForeignKeyMember(Member member);
    public Optional<Member> findById(Member member);
    public Optional<Member> findByUserId(Member member);
    public Optional<Member> findMemberByNameAndPhone(Member member);
    public CommonMemberForm memberToMemberForm(Member member);
    OauthMemberForm memberToOauthMemberForm(Member member);
    boolean validateDuplicatedMemberLoginId(String memberLoginId);
    boolean validateDuplicatedMemberNickname(CommonMemberForm form);
    boolean validateDuplicatedMemberNickname(CommonMemberForm form,long memberId);
    boolean validateDuplicatedMemberNickname(OauthMemberForm form,long memberId);
}

