package project.studycafe.contoller.form;

import lombok.*;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {

    @NotNull
    private String userLoginId;
    @NotNull
    private String userPassword;

    private String name;
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
