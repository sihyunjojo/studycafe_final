package project.studycafe.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.studycafe.app.domain.base.BaseTimeEntity;
import project.studycafe.app.domain.enums.status.DeliveryStatus;
import project.studycafe.app.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address = new Address();

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;


    public Delivery(Member member, Address address, DeliveryStatus deliveryStatus) {
        this.member = member;
        this.address = address;
        this.status = deliveryStatus;
    }

    @PrePersist
    void setting() {
        if (this.status == null) {
            this.status = DeliveryStatus.READY;
        }
    }


    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", order=" + order.getId() +
                ", address=" + address.toString() +
                ", status=" + status +
                '}';
    }
}
