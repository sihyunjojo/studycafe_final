package project.studycafe.app.repository.order;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import project.studycafe.app.domain.Order;
import project.studycafe.app.domain.enums.status.OrderStatus;
import project.studycafe.app.service.dto.searchDto.OrderSearchCond;


import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static project.studycafe.app.domain.QOrder.order;
import static project.studycafe.app.domain.enums.status.OrderStatus.*;


@Slf4j
public class OrderQueryRepository {
    private final JPAQueryFactory query;

    public OrderQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Order> findSearchedAndSortedOrder(OrderSearchCond cond) {
        return query.
                select(order).
                from(order).
                where(
                        likeMemberNickname(cond.getMemberNickname()),
                        likeProductName(cond.getProductName()),
                        eqProductCategory(cond.getProductCategory()),
                        eqOrderStatus(cond.getOrderStatus()),
                        leMaxCreatedTime(cond.getMaxCreatedTime()),
                        geMinCreatedTime(cond.getMinCreatedTime())
                ).
                orderBy(
                        sortedBoardBySort(cond.getSort())
                ).
                fetch();
    }

    private OrderSpecifier<?> sortedBoardBySort(String sort) {
        log.info("sort ={}", sort);
        if (StringUtils.hasText(sort)) {
            if ("orderIdUp".equalsIgnoreCase(sort)) {
                return order.id.asc();
            } else if ("orderIdDown".equalsIgnoreCase(sort)) {
                return order.id.desc();
            } else if ("orderStatusUp".equalsIgnoreCase(sort)) {
                return order.orderstatus.asc();
            } else if ("orderStatusDown".equalsIgnoreCase(sort)) {
                return order.orderstatus.desc();
            }
        }
        return order.id.desc();
    }


    private BooleanExpression likeMemberNickname(String memberNickname) {
        if (StringUtils.hasText(memberNickname)) {
            return order.member.nickname.like("%" + memberNickname + "%");
        }
        return null;
    }

    private BooleanExpression likeProductName(String productName) {
        if (StringUtils.hasText(productName)) {
            return order.orderItems.any().product.name.like("%" + productName + "%");
        }
        return null;
    }

    private BooleanExpression eqProductCategory(String category) {
        if (StringUtils.hasText(category)) {
            return order.orderItems.any().product.category.eq(category);
        }
        return null;
    }


    private BooleanExpression eqOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == WAIT) {
            return order.orderstatus.eq(WAIT);
        } else if (orderStatus == DELIVERING) {
            return order.orderstatus.eq(DELIVERING);
        } else if (orderStatus == CANCEL) {
            return order.orderstatus.eq(CANCEL);
        } else if (orderStatus == COMPLETE) {
            return order.orderstatus.eq(COMPLETE);
        }
        return null;
    }


    private BooleanExpression leMaxCreatedTime(String maxCreatedTime) {
        log.info(" max Item = {}", maxCreatedTime);
        if (maxCreatedTime != null && !maxCreatedTime.isEmpty()) {
            // Parse maxCreatedTime string into LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            LocalDateTime maxDateTime = LocalDate.parse(maxCreatedTime, formatter).atStartOfDay();
            // Perform the comparison using `loe` (less than or equal) method
            return order.createdTime.loe(maxDateTime);
        }
        return null;
    }


    private BooleanExpression geMinCreatedTime(String minCreatedTime) {
        log.info(" min time = {}", minCreatedTime);
        if (minCreatedTime != null && !minCreatedTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            LocalDateTime minDateTime = LocalDate.parse(minCreatedTime, formatter).atStartOfDay();
            return order.createdTime.goe(minDateTime);
        }
        return null;
    }
}

