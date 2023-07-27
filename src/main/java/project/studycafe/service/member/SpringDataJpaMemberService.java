package project.studycafe.service.member;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.contoller.form.CommonMemberForm;
import project.studycafe.contoller.form.MemberForm;
import project.studycafe.contoller.form.OauthMemberForm;
import project.studycafe.domain.*;
import project.studycafe.repository.member.JpaMemberRepository;

import java.util.List;
import java.util.Objects;
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
    public Object join(CommonMemberForm form) {
        Member member = new Member();
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        member.setUserLoginId(form.getUserLoginId());
        member.setUserPassword(form.getUserPassword());
        member.setName(form.getName());
        member.setNickname(form.getNickname());
        member.setPhone(form.getPhone());
        member.setEmail(form.getEmail());
        member.setGender(form.getGender());
        member.setBirth(form.getBirth());
        member.setAddress(address);

        try {
            validateDuplicatedMember(member); // 중복회원 검증
        } catch (IllegalStateException e) {
            log.info("회원 아이디가 중복되었습니다.");
            return false;
        }

        log.info("member ={}", member);
        memberRepository.save(member); // db에 멤버값 넣어줌
        return member.getId();
    }

    @Override
    public Optional<Member> update(long memberId, MemberForm form) throws NotFoundException {
        log.info("form = {}", form);
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException("Member not found with ID: " + memberId));

        if (form instanceof CommonMemberForm) {
            CommonMemberForm commonForm = (CommonMemberForm) form;
            Address address = new Address(commonForm.getCity(), commonForm.getStreet(), commonForm.getZipcode());

            findMember.setUserPassword(commonForm.getUserPassword());
            findMember.setName(commonForm.getName());
            findMember.setNickname(commonForm.getNickname());
            findMember.setGender(commonForm.getGender());
            findMember.setPhone(commonForm.getPhone());
            findMember.setEmail(commonForm.getEmail());
            findMember.setBirth(commonForm.getBirth());
            findMember.setAddress(address);
        } else if (form instanceof OauthMemberForm) {
            OauthMemberForm oauthForm = (OauthMemberForm) form;
            Address address = new Address(oauthForm.getCity(), oauthForm.getStreet(), oauthForm.getZipcode());

            log.info("form = {}", form);


            findMember.setName(oauthForm.getName());
            findMember.setNickname(oauthForm.getNickname());
            findMember.setGender(oauthForm.getGender());
            findMember.setPhone(oauthForm.getPhone());
            findMember.setEmail(oauthForm.getEmail());
            findMember.setBirth(oauthForm.getBirth());
            findMember.setAddress(address);
        }

        log.info("findMember = {}", findMember);

        return Optional.of(findMember);
    }

    @Override
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public void removeForeignKeyMember(Member member) {
        List<Board> boards = member.getBoards();
        for (Board board : boards) {
            log.info("board = {}", board);
            board.setMember(null);
        }

        List<Comment> comments = member.getComments();
        for (Comment comment : comments) {
            comment.setMember(null);
        }

        List<Reply> replies = member.getReplies();
        for (Reply reply : replies) {
            reply.setMember(null);
        }
    }

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

    // member 의 이름과 같은걸 찾아서
    // ifPresent()는 null 이 아니면 실행해줌.
    private void validateDuplicatedMember(Member member) {
        log.info("userId = {}", member.getUserLoginId());
        memberRepository.findFirstByUserLoginId(member.getUserLoginId())// member 과 같은 이름이 있는지 찾앗을때
                .ifPresent(member1 -> {    // null 이 아니니까(값이존재하면 ifPresent()인자 함수 실행
                    throw new IllegalStateException("이미 존재하는 회원입니다.");  //
                });
    }

    @Override
    public boolean validateDuplicatedMemberLoginId(String memberLoginId) {
        log.info("userId = {}", memberLoginId);
        return memberRepository.findFirstByUserLoginId(memberLoginId).isPresent();// member 과 같은 이름이 있는지 찾앗을때
    }

    @Override
    public boolean validateDuplicatedMemberNickname(CommonMemberForm form) {
        return memberRepository.findByNickname(form.getNickname()).isPresent();// member 과 같은 이름이 있는지 찾앗을때
    }

//    @Override
//    public boolean validateDuplicatedMemberNickname(MemberForm form, long memberId) {
//        // 기존 멤버 정보를 조회합니다.
//        Optional<Member> optionalMember = memberRepository.findById(memberId);
//
//        if (optionalMember.isPresent()) {
//            Member existingMember = optionalMember.get();
//            String newNickname = form.getNickname();
//
//            // 닉네임이 변경된 경우에만 중복 검사를 수행합니다.
//            if (!Objects.equals(existingMember.getNickname(), newNickname)) {
//                // 새로운 닉네임이 이미 사용 중인지 확인합니다. (자기자신 아님)
//                Optional<Member> duplicateMember = memberRepository.findByNickname(newNickname);
//                // 닉네임이 중복되는 멤버가 존재 하고 중복되는 멤버와 현재 멤버가 같지 않으면 true가 나와야함.
//                return duplicateMember.isPresent() && !duplicateMember.get().getId().equals(existingMember.getId());
//            }
//            return false; // 닉네임이 바뀌지 않았으니, 검증 안해도됨.
//        }
//
//        else{
//            log.info("기존 멤버가 존재하지 않습니다. 멤버가 사리지지 않았나 확인해보세요.");
//            return true;
//        }
//    }

    @Override
    public boolean validateDuplicatedMemberNickname(CommonMemberForm form, long memberId) {
        // 기존 멤버 정보를 조회합니다.
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();
            String newNickname = form.getNickname();

            // 닉네임이 변경된 경우에만 중복 검사를 수행합니다.
            if (!Objects.equals(existingMember.getNickname(), newNickname)) {
                // 새로운 닉네임이 이미 사용 중인지 확인합니다. (자기자신 아님)
                Optional<Member> duplicateMember = memberRepository.findByNickname(newNickname);
                // 닉네임이 중복되는 멤버가 존재 하고 중복되는 멤버와 현재 멤버가 같지 않으면 true가 나와야함.
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
    public boolean validateDuplicatedMemberNickname(OauthMemberForm form, long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();
            String newNickname = form.getNickname();
            log.info("form ={}", form);
            log.info("member = {}", existingMember);

            // 닉네임이 변경된 경우에만 중복 검사를 수행합니다.
            if (!Objects.equals(existingMember.getNickname(), newNickname)) {
                // 새로운 닉네임이 이미 사용 중인지 확인합니다. (자기자신 아님)
                Optional<Member> duplicateMember = memberRepository.findByNickname(newNickname);
                // 닉네임이 중복되는 멤버가 존재 하고 중복되는 멤버와 현재 멤버가 같지 않으면 true가 나와야함.
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
    public CommonMemberForm memberToMemberForm(Member member) {
        log.info("member = {}", member.getAddress());

        return new CommonMemberForm(member.getUserLoginId(), member.getUserPassword(),
                member.getName(), member.getNickname(), member.getGender(), member.getPhone(),
                member.getAddress().getCity(), member.getAddress().getStreet(), member.getAddress().getZipcode(),
                member.getEmail(), member.getBirth());
    }

    @Override
    public OauthMemberForm memberToOauthMemberForm(Member member) {
        return new OauthMemberForm(member.getEmail(), member.getProvider(),
                member.getName(), member.getNickname(), member.getGender(), member.getPhone(),
                member.getAddress().getCity(), member.getAddress().getStreet(), member.getAddress().getZipcode(),
                member.getBirth());
    }
}
