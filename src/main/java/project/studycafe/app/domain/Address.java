package project.studycafe.app.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter @ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Address {

    private String city = "";
    private String street = "";
    private String zipcode = "";

    // 이렇게 안하니까 null값이 계속 들어가더라.
    public Address(String city, String street, String zipcode) {
        this.city = city != null ? city : "";
        this.street = street != null ? street : "";
        this.zipcode = zipcode != null ? zipcode : "";
    }
}
