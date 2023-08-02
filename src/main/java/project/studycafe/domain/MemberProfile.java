package project.studycafe.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.studycafe.domain.Member;

@Getter @Setter
@ToString
public class MemberProfile {
    private String name;
    private String email;
    private String provider;
    private String nickname;

    public Member toMember() {
        return Member.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .build();
    }

}