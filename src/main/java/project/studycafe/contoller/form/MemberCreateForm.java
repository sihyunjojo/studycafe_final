package project.studycafe.contoller.form;

import lombok.*;

import javax.validation.constraints.Email;

@Data
public class MemberCreateForm {
    private String userLoginId;
    private String userPassword;
    private String name;
    private String nickname;
    private String phone;
    private String address;

    @Email
    private String email;
    private String gender;
    private String birth;

}
