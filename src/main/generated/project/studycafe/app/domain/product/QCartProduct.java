package project.studycafe.app.domain.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartProduct is a Querydsl query type for CartProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartProduct extends EntityPathBase<CartProduct> {

    private static final long serialVersionUID = 691376656L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartProduct cartProduct = new QCartProduct("cartProduct");

    public final project.studycafe.app.domain.base.QBaseTimeEntity _super = new project.studycafe.app.domain.base.QBaseTimeEntity(this);

    public final project.studycafe.app.domain.QCart cart;

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProduct product;

    public final BooleanPath purchasedCheck = createBoolean("purchasedCheck");

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QCartProduct(String variable) {
        this(CartProduct.class, forVariable(variable), INITS);
    }

    public QCartProduct(Path<? extends CartProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartProduct(PathMetadata metadata, PathInits inits) {
        this(CartProduct.class, metadata, inits);
    }

    public QCartProduct(Class<? extends CartProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new project.studycafe.app.domain.QCart(forProperty("cart"), inits.get("cart")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

