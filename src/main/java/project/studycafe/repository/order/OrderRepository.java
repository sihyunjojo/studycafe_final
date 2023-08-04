package project.studycafe.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.studycafe.domain.Order;
import project.studycafe.domain.member.Member;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByUpdatedTimeDesc();
    List<Order> findAllByMember(Member member);
}
