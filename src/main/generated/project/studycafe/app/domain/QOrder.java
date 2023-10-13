package project.studycafe.app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 532743726L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final project.studycafe.app.domain.base.QBaseTimeEntity _super = new project.studycafe.app.domain.base.QBaseTimeEntity(this);

    public final QAddress address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final EnumPath<project.studycafe.app.domain.enums.status.DeliveryStatus> deliveryStatus = createEnum("deliveryStatus", project.studycafe.app.domain.enums.status.DeliveryStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.studycafe.app.domain.member.QMember member;

    public final ListPath<project.studycafe.app.domain.product.OrderItem, project.studycafe.app.domain.product.QOrderItem> orderItems = this.<project.studycafe.app.domain.product.OrderItem, project.studycafe.app.domain.product.QOrderItem>createList("orderItems", project.studycafe.app.domain.product.OrderItem.class, project.studycafe.app.domain.product.QOrderItem.class, PathInits.DIRECT2);

    public final EnumPath<project.studycafe.app.domain.enums.status.OrderStatus> orderstatus = createEnum("orderstatus", project.studycafe.app.domain.enums.status.OrderStatus.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
        this.member = inits.isInitialized("member") ? new project.studycafe.app.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

