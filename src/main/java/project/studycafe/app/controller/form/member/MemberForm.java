package project.studycafe.app.controller.form.member;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface MemberForm {
    public String getNickname();
    public String getName();

    public String getUserLoginId();
    public String getUserPassword();


    public String getCity();
    public String getStreet();
    public String getZipcode();

    public String getGender();
    public String getPhone();
    public String getEmail();
    public String getBirth();


}
