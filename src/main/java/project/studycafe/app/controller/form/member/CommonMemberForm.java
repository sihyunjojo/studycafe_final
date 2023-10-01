package project.studycafe.app.controller.form.member;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMemberForm implements MemberForm {

    @NotBlank
    private String userLoginId;
    @NotBlank
    private String userPassword;

    @NotBlank(message = "회원 이름은 필수 입니다")
    private String name;
    @NotBlank(message = "회원 닉네임은 필수 입니다")
    private String nickname;
    private String gender;

    @NotBlank
    private String phone;

    private String city;
    private String street;
    private String zipcode;

    @Email
    private String email;
    private String birth;

    public CommonMemberForm(String userLoginId, String userPassword, String name, String nickname, String phone) {
        this.userLoginId = userLoginId;
        this.userPassword = userPassword;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
    }
}
