package project.studycafe.app.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1048789106L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final project.studycafe.app.domain.base.QBaseTimeEntity _super = new project.studycafe.app.domain.base.QBaseTimeEntity(this);

    public final project.studycafe.app.domain.QAddress address;

    public final StringPath birth = createString("birth");

    public final ListPath<project.studycafe.app.domain.board.Board, project.studycafe.app.domain.board.QBoard> boards = this.<project.studycafe.app.domain.board.Board, project.studycafe.app.domain.board.QBoard>createList("boards", project.studycafe.app.domain.board.Board.class, project.studycafe.app.domain.board.QBoard.class, PathInits.DIRECT2);

    public final project.studycafe.app.domain.QCart cart;

    public final ListPath<project.studycafe.app.domain.board.Comment, project.studycafe.app.domain.board.QComment> comments = this.<project.studycafe.app.domain.board.Comment, project.studycafe.app.domain.board.QComment>createList("comments", project.studycafe.app.domain.board.Comment.class, project.studycafe.app.domain.board.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final ListPath<project.studycafe.app.domain.Delivery, project.studycafe.app.domain.QDelivery> deliveries = this.<project.studycafe.app.domain.Delivery, project.studycafe.app.domain.QDelivery>createList("deliveries", project.studycafe.app.domain.Delivery.class, project.studycafe.app.domain.QDelivery.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<project.studycafe.app.domain.enums.MemberLevel> memberLevel = createEnum("memberLevel", project.studycafe.app.domain.enums.MemberLevel.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final ListPath<project.studycafe.app.domain.Order, project.studycafe.app.domain.QOrder> orders = this.<project.studycafe.app.domain.Order, project.studycafe.app.domain.QOrder>createList("orders", project.studycafe.app.domain.Order.class, project.studycafe.app.domain.QOrder.class, PathInits.DIRECT2);

    public final StringPath phone = createString("phone");

    public final StringPath provider = createString("provider");

    public final ListPath<project.studycafe.app.domain.board.Reply, project.studycafe.app.domain.board.QReply> replies = this.<project.studycafe.app.domain.board.Reply, project.studycafe.app.domain.board.QReply>createList("replies", project.studycafe.app.domain.board.Reply.class, project.studycafe.app.domain.board.QReply.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public final StringPath userLoginId = createString("userLoginId");

    public final StringPath userPassword = createString("userPassword");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new project.studycafe.app.domain.QAddress(forProperty("address")) : null;
        this.cart = inits.isInitialized("cart") ? new project.studycafe.app.domain.QCart(forProperty("cart"), inits.get("cart")) : null;
    }

}

