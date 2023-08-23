package project.studycafe.app.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.member.Member;

import javax.persistence.*;

import static project.studycafe.app.domain.Address.createAddress;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
//    private Order order;

    @Embedded
    private Address address = new Address();

    @PrePersist
    public void setting(){
        if (address == null) {
            address = createAddress("", "", "");
        }
    }

    public static Delivery createDelivery(Member member, Address address) {
        Delivery delivery = new Delivery();
        delivery.member = member;
        delivery.address = address;

        return delivery;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", address=" + address.toString() +
                '}';
    }
}
