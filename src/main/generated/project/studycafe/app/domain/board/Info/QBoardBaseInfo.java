package project.studycafe.app.domain.board.Info;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardBaseInfo is a Querydsl query type for BoardBaseInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBoardBaseInfo extends BeanPath<BoardBaseInfo> {

    private static final long serialVersionUID = 1779141613L;

    public static final QBoardBaseInfo boardBaseInfo = new QBoardBaseInfo("boardBaseInfo");

    public final StringPath category = createString("category");

    public final StringPath content = createString("content");

    public final StringPath title = createString("title");

    public QBoardBaseInfo(String variable) {
        super(BoardBaseInfo.class, forVariable(variable));
    }

    public QBoardBaseInfo(Path<? extends BoardBaseInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardBaseInfo(PathMetadata metadata) {
        super(BoardBaseInfo.class, metadata);
    }

}

