package project.studycafe.app.service.member;

import javassist.NotFoundException;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.controller.form.member.MemberForm;
import project.studycafe.app.controller.form.member.OauthMemberForm;
import project.studycafe.app.domain.member.Member;

import java.util.Optional;

public interface MemberService {

    public Long join(CommonMemberForm form);
    public Optional<Member> update(long memberId, MemberForm form);
//    public void removeForeignKeyMember(Member member);
    public Optional<Member> findById(long memberId);
    public Optional<Member> findByUserId(String memberLoginId);
    public Optional<Member> findMemberByNameAndPhone(String memberName, String memberPhone);

    public Optional<Member> findMemberByEmailAndProvider(String email, String provider);

    public void deleteMember(Member member);


    public CommonMemberForm memberToCommonMemberForm(Member member);
    public OauthMemberForm memberToOauthMemberForm(Member member);


    boolean validateDuplicatedMemberLoginId(String memberLoginId);
    boolean validateDuplicatedMemberLoginId(String memberLoginId, long memberId);

    boolean validateDuplicatedMemberNickname(MemberForm form);
    boolean validateDuplicatedMemberNickname(MemberForm form, long memberId);

}

