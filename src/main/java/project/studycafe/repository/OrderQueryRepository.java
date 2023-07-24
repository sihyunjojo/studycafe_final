package project.studycafe.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import project.studycafe.domain.Order;
import project.studycafe.domain.OrderStatus;


import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static project.studycafe.domain.QOrder.order;


public class OrderQueryRepository {
    private final JPAQueryFactory query;

    public OrderQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Order> findSearchedAndSortedOrder(OrderSearchCond cond) {
        return query.select(order)
                .from(order)
                .where(
                        likeMemberName(cond.getMemberName()),
                        likeProductName(cond.getProductName()),
                        eqProductCategory(cond.getCategory()),
                        eqOrderStatus(cond.getOrderStatus()),
                        leMaxCreatedTime(cond.getMaxCreatedTime()),
                        geMinCreatedTime(cond.getMinCreatedTime())
                )
                .fetch();
    }

    private BooleanExpression likeMemberName(String productName) {
        if (StringUtils.hasText(productName)) {
            return order.member.name.like("%" + productName + "%");
        }
        return null;
    }

    private BooleanExpression likeProductName(String productName) {
        if (StringUtils.hasText(productName)) {
            return order.orderItems.any().product.name.like("%" + productName + "%");
        }
        return null;
    }

    private Predicate eqProductCategory(String category) {
        if (StringUtils.hasText(category)) {
            return order.orderItems.any().product.category.eq(category);
        }
        return null;
    }


    private Predicate eqOrderStatus(String orderStatus) {
        if (StringUtils.hasText(orderStatus)) {
            if ("WAIT".equalsIgnoreCase(orderStatus)) {
                return order.status.eq(OrderStatus.WAIT);
            } else if ("CANCEL".equalsIgnoreCase(orderStatus)) {
                return order.status.eq(OrderStatus.CANCEL);
            } else if ("COMPLETE".equalsIgnoreCase(orderStatus)) {
                return order.status.eq(OrderStatus.COMPLETE);
            }
        }
        return null;
    }


    private BooleanExpression leMaxCreatedTime(LocalDateTime maxCreatedTime) {
        if (maxCreatedTime != null) {
            return order.createdTime.loe(maxCreatedTime);
        }
        return null;
    }

    private BooleanExpression geMinCreatedTime(LocalDateTime minCreatedTime) {
        if (minCreatedTime != null) {
            return order.createdTime.goe(minCreatedTime);
        }
        return null;
    }


}
