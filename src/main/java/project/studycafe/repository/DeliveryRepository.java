package project.studycafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studycafe.domain.Address;
import project.studycafe.domain.Delivery;
import project.studycafe.domain.Member;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Boolean existsDistinctByAddressAndMember(Address address, Member member);
}
