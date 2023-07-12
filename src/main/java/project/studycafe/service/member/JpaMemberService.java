package project.studycafe.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.MemberCreateForm;
import project.studycafe.domain.Member;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.contoller.form.MemberUpdateForm;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class JpaMemberService implements MemberService {
    private final JpaMemberRepository memberRepository;

    /**
     * 회원가입
     */
    public String join(MemberCreateForm form) {
        Member member = new Member();
        member.setUserLoginId(form.getUserLoginId());


        try {
            validateDuplicatedMember(member); // 중복회원 검증
        } catch (IllegalStateException e) {
            return null;
        }
        memberRepository.save(member); // db에 멤버값 넣어줌
        return member.getUserLoginId();
    }

    //public Member viewMember(Member member);


    @Override
    public Optional<Member> findById(Member member) {
        return memberRepository.findById(member.getId());
    }

    public Optional<Member> findByUserId(Member member){
        return memberRepository.findFirstByUserLoginId(member.getUserLoginId());
    }

    public Optional<Member> findMemberByNameAndPhone(Member member){
        return memberRepository.findFirstByNameAndPhone(member.getName(), member.getPhone());
    }

    @Override
    public Optional<Member> update(long memberId, MemberUpdateForm updateForm) {
        return null;
    }


    //public int checkMemberPassword(Member member);
    //public Member getFindPasswordMember(Member member);
    //public int checkUniqueId(Member member);


    // memeber의 이름과 같은걸 찾아서
    private void validateDuplicatedMember(Member member) {
        log.info("userId = {}", member.getUserLoginId());
        memberRepository.findFirstByUserLoginId(member.getUserLoginId())// member과 같은 이름이 있는지 찾앗을때
                .ifPresent(member1 -> {    // null이 아니니까(값이존재하면 ifPresent()인자 함수 실행
                    throw new IllegalStateException("이미 존재하는 회원입니다.");  //
                });
    }

}
