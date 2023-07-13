package project.studycafe.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.MemberCreateForm;
import project.studycafe.domain.Address;
import project.studycafe.domain.Member;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.contoller.form.MemberUpdateForm;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SpringDataJpaMemberService implements MemberService {
    private final JpaMemberRepository memberRepository;

    /**
     * 회원가입
     */
    public Object join(MemberCreateForm form) {
        Member member = new Member();
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        member.setUserLoginId(form.getUserLoginId());
        member.setUserPassword(form.getUserPassword());
        member.setName(form.getName());
        member.setNickname(form.getNickname());
        member.setPhone(form.getPhone());
        member.setEmail(form.getEmail());
        member.setBirth(form.getBirth());
        member.setAddress(address);

        try {
            validateDuplicatedMember(member); // 중복회원 검증
        } catch (IllegalStateException e) {
            log.info("회원 아이디가 중복되었습니다.");
            return false;
        }

        memberRepository.save(member); // db에 멤버값 넣어줌
        return member.getId();
    }

    @Override
    public Optional<Member> update(long memberId, MemberUpdateForm form) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(); //값이 없으며 에러를 내라. 요고군
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        findMember.setUserPassword(form.getUserPassword());
        findMember.setName(form.getName());
        findMember.setNickname(form.getNickname());
        findMember.setGender(form.getGender());
        findMember.setPhone(form.getPhone());
        findMember.setEmail(form.getEmail());
        findMember.setBirth(form.getBirth());
        findMember.setAddress(address);

        log.info("findMember ={}", findMember);

        return Optional.of(findMember);
    }

    //public Member viewMember(Member member);

    @Override
    public Optional<Member> findById(Member member) {
        log.info("member={}", member);
        return memberRepository.findById(member.getId());
    }

    public Optional<Member> findByUserId(Member member){
        return memberRepository.findFirstByUserLoginId(member.getUserLoginId());
    }

    public Optional<Member> findMemberByNameAndPhone(Member member){
        return memberRepository.findFirstByNameAndPhone(member.getName(), member.getPhone());
    }

    public Optional<Member> findByEmailAndProvider(String email, String provider) {
        return memberRepository.findByEmailAndProvider(email,provider);
    }

    //public int checkMemberPassword(Member member);
    //public Member getFindPasswordMember(Member member);
    //public int checkUniqueId(Member member);

    // memeber의 이름과 같은걸 찾아서
    // ifPresent()는 null이 아니면 실행해줌.
    private void validateDuplicatedMember(Member member) {
        log.info("userId = {}", member.getUserLoginId());
        memberRepository.findFirstByUserLoginId(member.getUserLoginId())// member과 같은 이름이 있는지 찾앗을때
                .ifPresent(member1 -> {    // null이 아니니까(값이존재하면 ifPresent()인자 함수 실행
                    throw new IllegalStateException("이미 존재하는 회원입니다.");  //
                });
    }

    @Override
    public Boolean validateDuplicatedMemberLoginId(String memberLoginId) {
        log.info("userId = {}", memberLoginId);
        return memberRepository.findFirstByUserLoginId(memberLoginId).isPresent();// member과 같은 이름이 있는지 찾앗을때
    }

}
