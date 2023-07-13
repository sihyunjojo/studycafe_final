package project.studycafe.contoller.form;

import lombok.*;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MemberCreateForm {

    @NotNull
    private String userLoginId;
    @NotNull
    private String userPassword;
    @NotNull
    private String name;
    private String nickname;
    @NotNull
    private String phone;

    private String city;
    private String street;
    private String zipcode;

    @Email
    private String email;
    private String gender;
    private String birth;

}
