package project.studycafe.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.domain.form.member.CommonMemberForm;
import project.studycafe.domain.product.OrderItem;
import project.studycafe.domain.product.Product;
import project.studycafe.service.member.MemberService;

import java.util.ArrayList;
import java.util.List;

//데이터를 데이터베이스에 삽입할 수 있으며 변경 사항은 테스트 종료 시 자동으로 롤백됩니다.
@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test") //프로파일을 지정할 때 사용됩니다. application-{profile}.properties
public class OrderServiceTest {

    OrderItem item1;
    OrderItem item2;
    OrderItem item3;

    @Autowired
    OrderService orderService;
    @Autowired
    MemberService memberService;

    @BeforeEach
    public void setting() {
        Long memberId = memberService.join(new CommonMemberForm());

        item1 = OrderItem.createOrderItem(new Product(1L, "a", 1000, 100), 1);
        item2 = OrderItem.createOrderItem(new Product(2L, "b", 2000, 100), 2);
        item3 = OrderItem.createOrderItem(new Product(3L, "c", 3000, 100), 3);

    }


    @Test
    @DisplayName("OrderToTalPrice Test")
    public void testTotalPriceCalculation() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(item1);
        orderItems.add(item2);
        orderItems.add(item3);
        log.info("{}", item3.getCount());

//        Order order = Order.createOrder()

    }

}