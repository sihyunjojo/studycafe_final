package project.studycafe.app.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -1723874114L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final project.studycafe.app.domain.base.QBaseTimeEntity _super = new project.studycafe.app.domain.base.QBaseTimeEntity(this);

    public final project.studycafe.app.domain.board.Info.QBoardAddInfo boardAddInfo;

    public final project.studycafe.app.domain.board.Info.QBoardBaseInfo boardBaseInfo;

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.studycafe.app.domain.member.QMember member;

    public final project.studycafe.app.domain.base.QStatistics statistics;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.boardAddInfo = inits.isInitialized("boardAddInfo") ? new project.studycafe.app.domain.board.Info.QBoardAddInfo(forProperty("boardAddInfo")) : null;
        this.boardBaseInfo = inits.isInitialized("boardBaseInfo") ? new project.studycafe.app.domain.board.Info.QBoardBaseInfo(forProperty("boardBaseInfo")) : null;
        this.member = inits.isInitialized("member") ? new project.studycafe.app.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
        this.statistics = inits.isInitialized("statistics") ? new project.studycafe.app.domain.base.QStatistics(forProperty("statistics")) : null;
    }

}

