package project.studycafe.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.Member;
import project.studycafe.repository.member.JpaMemberRepository;
import project.studycafe.repository.member.dto.MemberUpdateForm;

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
    public String join(Member member) {
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

    @Override
    public Optional<Member> update(long memberId, MemberUpdateForm updateForm) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(); //값이 없으며 에러를 내라. 요고군

        findMember.setUserPassword(updateForm.getUserPassword());
        findMember.setName(updateForm.getName());
        findMember.setGender(updateForm.getGender());
        findMember.setPhone(updateForm.getPhone());
        findMember.setBirth(updateForm.getBirth());
        findMember.setAddress(updateForm.getAddress());
        findMember.setEmail(updateForm.getEmail());

        log.info("findMember ={}", findMember);

        return Optional.of(findMember);
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


}
