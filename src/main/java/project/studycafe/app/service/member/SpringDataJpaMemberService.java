package project.studycafe.app.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.controller.form.member.MemberForm;
import project.studycafe.app.controller.form.member.OauthMemberForm;
import project.studycafe.app.domain.Address;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.repository.member.JpaMemberRepository;
import project.studycafe.helper.exception.member.NotFoundMemberException;

import java.util.Objects;
import java.util.Optional;

import static project.studycafe.app.domain.Address.createAddress;
import static project.studycafe.app.domain.Cart.createCart;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SpringDataJpaMemberService implements MemberService {
    private final JpaMemberRepository memberRepository;

    public Long join(CommonMemberForm form) {
        Member member = new Member();
        Address address = createAddress(form.getCity(), form.getStreet(), form.getZipcode());

        member.setUserLoginId(form.getUserLoginId());
        member.setUserPassword(form.getUserPassword());
        member.setName(form.getName());
        member.setNickname(form.getNickname());
        member.setPhone(form.getPhone());
        member.setEmail(form.getEmail());
        member.setGender(form.getGender());
        member.setBirth(form.getBirth());
        member.setAddress(address);
        member.setCart(createCart(member));

        try {
            validateDuplicatedMember(member); // 중복회원 검증
        } catch (IllegalStateException e) {
            log.info("회원 아이디가 중복되었습니다.");
            // 어자피 bindingResult 를 통해서 클라이언트에게 알려줌.
        }

        log.info("member ={}", member);
        memberRepository.save(member); // db에 멤버값 넣어줌
        return member.getId();
    }

    @Override
    public Optional<Member> update(long memberId, MemberForm form){
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundMemberException("Member not found with ID: " + memberId));

            Address address = createAddress(form.getCity(), form.getStreet(), form.getZipcode());

            findMember.setName(form.getName());
            findMember.setNickname(form.getNickname());
            findMember.setUserLoginId(form.getUserLoginId());
            findMember.setUserPassword(form.getUserPassword());
            findMember.setGender(form.getGender());
            findMember.setPhone(form.getPhone());
            findMember.setEmail(form.getEmail());
            findMember.setBirth(form.getBirth());
            findMember.setAddress(address);

        return Optional.of(findMember);
    }

    @Override
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public Optional<Member> findById(long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findByUserId(String memberLoginId){
        return memberRepository.findFirstByUserLoginId(memberLoginId);
    }

    public Optional<Member> findMemberByNameAndPhone(String memberName, String memberPhone){
        return memberRepository.findFirstByNameAndPhone(memberName, memberPhone);
    }

    public Optional<Member> findMemberByEmailAndProvider(String email, String provider) {
        return memberRepository.findByEmailAndProvider(email, provider);
    }

    // member 의 이름과 같은걸 찾아서
    // ifPresent()는 null 이 아니면 실행해줌.
    private void validateDuplicatedMember(Member member) {
        log.info("userLoginId = {}", member.getUserLoginId());
        memberRepository.findFirstByUserLoginId(member.getUserLoginId())// member 과 같은 이름이 있는지 찾앗을때
                .ifPresent(member1 -> {    // null 이 아니니까(값이존재하면 ifPresent()인자 함수 실행
                    throw new IllegalStateException("이미 존재하는 회원입니다.");  //
                });
    }

    @Override
    public boolean validateDuplicatedMemberLoginId(String memberLoginId) {
        return memberRepository.findFirstByUserLoginId(memberLoginId).isPresent();// member 과 같은 이름이 있는지 찾앗을때
    }

    @Override
    public boolean validateDuplicatedMemberLoginId(String newLoginId, long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();

            // 아이디가 변경된 경우에만 중복 검사를 수행합니다.
            if (!Objects.equals(existingMember.getUserLoginId(), newLoginId)) {
                Optional<Member> validateMember = memberRepository.findFirstByUserLoginId(newLoginId);
                // 닉네임이 중복되는 멤버가 존재 하고  중복되는 멤버와 현재 멤버가 같지 않으면 true 가 나와야함.
                // 그럼 중복이 되는거임.
                return validateMember.isPresent() && !validateMember.get().getId().equals(existingMember.getId());
            }
            return false;
        }

        else{
            log.info("기존 멤버가 존재하지 않습니다. 멤버가 사리지지 않았나 확인해보세요.");
            throw new RuntimeException("기존 멤버가 존재하지 않습니다. 멤버가 사리지지 않았나 확인해보세요.");
        }
    }

    @Override
    public boolean validateDuplicatedMemberNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();// member 과 같은 이름이 있는지 찾앗을때
    }

    @Override
    public boolean validateDuplicatedMemberNickname(String newNickname, long memberId) {
        // 기존 멤버 정보를 조회합니다.
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();

            // 닉네임이 변경된 경우에만 중복 검사를 수행합니다.
            if (!Objects.equals(existingMember.getNickname(), newNickname)) {
                // 새로운 닉네임이 이미 사용 중인지 확인합니다. (자기자신 아님)
                Optional<Member> duplicateMember = memberRepository.findByNickname(newNickname);
                // 닉네임이 중복되는 멤버가 존재 하고 중복되는 멤버와 현재 멤버가 같지 않으면 true 가 나와야함.
                return duplicateMember.isPresent() && !duplicateMember.get().getId().equals(existingMember.getId());
            }
            return false; // 닉네임이 바뀌지 않았으니, 검증 안해도됨.
        }

        else{
            log.info("기존 멤버가 존재하지 않습니다. 멤버가 사리지지 않았나 확인해보세요.");
            return true;
        }
    }

    @Override
    public CommonMemberForm memberToCommonMemberForm(Member member) {
        return new CommonMemberForm(member.getUserLoginId(), member.getUserPassword(),
                member.getName(), member.getNickname(), member.getGender(), member.getPhone(),
                member.getAddress().getCity(), member.getAddress().getStreet(), member.getAddress().getZipcode(),
                member.getEmail(), member.getBirth());
    }

    @Override
    public OauthMemberForm memberToOauthMemberForm(Member member) {
        return new OauthMemberForm(member.getEmail(), member.getProvider(),
                member.getUserLoginId(), member.getUserPassword(),
                member.getName(), member.getNickname(), member.getGender(), member.getPhone(),
                member.getAddress().getCity(), member.getAddress().getStreet(), member.getAddress().getZipcode(),
                member.getBirth());
    }
}
