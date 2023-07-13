package project.studycafe.contoller.form;

import lombok.Data;
import project.studycafe.domain.Address;

import javax.persistence.Embedded;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberUpdateForm {

    @NotEmpty
    private String userPassword;
    @NotEmpty
    private String checkPassword;
    @NotEmpty
    private String name;
    private String nickname;
    private String gender;
    @NotEmpty
    private String phone;

    @Email
    private String email;

    private String birth;

    private String city;
    private String street;
    private String zipcode;

    @Override
    public String toString() {
        return "MemberUpdateForm{" +
                ", userPassword='" + userPassword + '\'' +
                ", checkPassword='" + checkPassword + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", birth='" + birth + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
