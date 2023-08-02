package project.studycafe.contoller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthMemberForm implements MemberForm{

    @Email
    private String email;
    private String provider;

    private String name;
    @NotEmpty(message = "회원 닉네임은 필수 입니다")
    private String nickname;
    private String gender;

    @NotNull
    private String phone;

    private String city;
    private String street;
    private String zipcode;

    private String birth;

}
