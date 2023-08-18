package project.studycafe.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.app.domain.Address;
import project.studycafe.app.domain.Delivery;
import project.studycafe.app.domain.member.Member;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Boolean existsDistinctByAddressAndMember(Address address, Member member);
    Boolean existsDistinctByAddress(Address address);
    Boolean existsDistinctByMember(Member member);

}
