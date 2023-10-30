package project.studycafe.app.controller.form.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data

public class OauthMemberForm implements MemberForm {

    @Email
    private String email;
    private String provider;

    private String userLoginId;
    private String userPassword;

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    @NotEmpty(message = "회원 닉네임은 필수 입니다")
    private String nickname;
    private String gender;

    private String phone;

    private String city;
    private String street;
    private String zipcode;

    private String birth;



    @Builder
    public OauthMemberForm(String email, String provider, String userLoginId, String userPassword, String name,
                           String nickname, String gender, String phone, String city, String street, String zipcode,
                           String birth) {
        this.email = email;
        this.provider = provider;
        this.userLoginId = userLoginId;
        this.userPassword = userPassword;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.phone = phone;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.birth = birth;
    }
}
