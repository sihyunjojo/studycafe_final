package project.studycafe.repository.product;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import project.studycafe.domain.Product;
import project.studycafe.repository.product.dto.ProductSearchCond;

import javax.persistence.EntityManager;
import java.util.List;

import static project.studycafe.domain.QBoard.board;
import static project.studycafe.domain.QProduct.product;

public class JpaQueryProductRepository {
    private final JPAQueryFactory query;

    public JpaQueryProductRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Product> findSearchedAndSortedProducts(ProductSearchCond cond) {
        return query.select(product)
                .from(product)
                .where(
                        likeProductName(cond.getName()),
                        eqProductCategory(cond.getCategory()),
                        leMaxPrice(cond.getMaxPrice()),
                        geMinPrice(cond.getMinPrice()),
                        geLikeCount(cond.getMinLikeCount())
                )
                .orderBy(
                        sortedProductBySort(cond.getSort()),
                        product.likeCount.desc()
                )
                .fetch();
    }

    public List<Product> findTop5LikeCountProducts(ProductSearchCond cond) {
        return query.select(product)
                .from(product)
                .where(
                        eqProductCategory(cond.getCategory())
                )
                .orderBy(product.likeCount.desc())
                .limit(5)
                .fetch();
    }

    private OrderSpecifier<?> sortedProductBySort(String sort) {
        if (StringUtils.hasText(sort)) {
            if ("productReadCountUp".equalsIgnoreCase(sort)) {
                return product.readCount.asc();
            } else if ("productReadCountDown".equalsIgnoreCase(sort)) {
                return product.readCount.desc();
            } else if ("productLikeCountUp".equalsIgnoreCase(sort)) {
                return product.likeCount.asc();
            } else if ("productLikeCountDown".equalsIgnoreCase(sort)) {
                return product.likeCount.desc();
            }
        }
        return product.createdTime.desc();
    }

    private BooleanExpression likeProductName(String productName) {
        if (StringUtils.hasText(productName)) {
            return product.name.like("%" + productName + "%");
        }
        return null;
    }

    private Predicate eqProductCategory(String category) {
        if (StringUtils.hasText(category)) {
            return product.category.eq(category);
        }
        return null;
    }

    private BooleanExpression leMaxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return product.price.loe(maxPrice);
        }
        return null;
    }

    private BooleanExpression geMinPrice(Integer minPrice) {
        if (minPrice != null) {
            return product.price.goe(minPrice);
        }
        return null;
    }

    private BooleanExpression geLikeCount(Integer likeCount) {
        if (likeCount != null) {
            return product.likeCount.goe(likeCount);
        }
        return null;
    }


}
