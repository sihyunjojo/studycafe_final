package project.studycafe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@ToString
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){

    }

}
