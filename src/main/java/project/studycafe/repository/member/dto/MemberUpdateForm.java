package project.studycafe.repository.member.dto;

import lombok.Data;
import project.studycafe.domain.Address;

import javax.persistence.Embedded;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberUpdateForm {

    @NotEmpty
    private String userLoginId;
    @NotEmpty
    private String userPassword;
    @NotEmpty
    private String checkPassword;
    @NotEmpty
    private String name;
    @NotEmpty
    private String gender;
    @NotEmpty
    private String phone;

    @Email
    private String email;

    private String birth;
    private String nickname;

    @Embedded
    private Address address;

    @Override
    public String toString() {
        return "MemberUpdateForm{" +
                "userLoginId='" + userLoginId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", checkPassword='" + checkPassword + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", birth='" + birth + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
