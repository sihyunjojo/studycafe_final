package project.studycafe.domain.form.member;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMemberForm implements MemberForm {

    @NotNull
    private String userLoginId;
    @NotNull
    private String userPassword;

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    @NotEmpty(message = "회원 닉네임은 필수 입니다")
    private String nickname;
    private String gender;

    @NotNull
    private String phone;

    private String city;
    private String street;
    private String zipcode;

    @Email
    private String email;
    private String birth;

}