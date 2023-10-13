package project.studycafe.app.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter @ToString
public class Address {

    private String city = "";
    private String street = "";
    private String zipcode = "";

    protected Address() {
        this.city = "";
        this.street = "";
        this.zipcode = "";
    }

    // 이렇게 안하니까 null값이 계속 들어가더라.
    public static Address createAddress(String city, String street, String zipcode) {
        Address address = new Address();
        address.city = city != null ? city : "";
        address.street = street != null ? street : "";
        address.zipcode = zipcode != null ? zipcode : "";

        return address;
    }
}
