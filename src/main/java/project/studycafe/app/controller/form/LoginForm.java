package project.studycafe.app.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty
    private String userLoginId;

    @NotEmpty
    private String userPassword;

}
