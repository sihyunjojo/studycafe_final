package project.studycafe.app.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.studycafe.app.controller.form.order.OrderNowForm;
import project.studycafe.app.domain.member.Member;
import project.studycafe.app.service.OrderService;
import project.studycafe.app.domain.Order;
import project.studycafe.app.controller.form.member.CommonMemberForm;
import project.studycafe.app.domain.product.OrderItem;
import project.studycafe.app.domain.product.Product;
import project.studycafe.app.service.member.MemberService;
import project.studycafe.app.service.product.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//데이터를 데이터베이스에 삽입할 수 있으며 변경 사항은 테스트 종료 시 자동으로 롤백됩니다.
@Slf4j
@Transactional
@SpringBootTest
public class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired MemberService memberService;
    @Autowired ProductService productService;


    Member member;
    Long memberId;

    OrderItem item1;
    OrderItem item2;
    OrderItem item3;


    @BeforeEach
    public void setting() {
        // 기본으로 member을 만들어서 제공헤주고
        CommonMemberForm commonMemberForm = new CommonMemberForm("1", "1",
                "1", "1", "01085524018");
        memberId = memberService.join(commonMemberForm);

        Optional<Member> findMember = memberService.findById(memberId);
        member = findMember.orElseThrow();


        //
        item1 = OrderItem.createOrderItem(new Product( "a","과자", 1000, 100), 1);
        item2 = OrderItem.createOrderItem(new Product( "b", "음료",2000, 100), 2);
        item3 = OrderItem.createOrderItem(new Product( "c", "과자", 3000, 100), 3);

    }


    @Test
    @DisplayName("바로 주문하기")
    public void addOrderNow(){
        //when
        //product를 만들고 해야할거같음.
//        OrderNowForm orderNowForm = new OrderNowForm("");

        //given
//        orderService.addOrderNow();
        //then
    }
    @Test
    @DisplayName("OrderToTalPrice Test")
    public void testTotalPriceCalculation() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(item1);
        orderItems.add(item2);
        orderItems.add(item3);
        log.info("{}", item3.getCount());

    }

}