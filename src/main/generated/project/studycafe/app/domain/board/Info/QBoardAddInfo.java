package project.studycafe.app.domain.board.Info;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardAddInfo is a Querydsl query type for BoardAddInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBoardAddInfo extends BeanPath<BoardAddInfo> {

    private static final long serialVersionUID = -66139551L;

    public static final QBoardAddInfo boardAddInfo = new QBoardAddInfo("boardAddInfo");

    public final ListPath<project.studycafe.app.domain.board.AttachmentFile, project.studycafe.app.domain.board.QAttachmentFile> attachmentFiles = this.<project.studycafe.app.domain.board.AttachmentFile, project.studycafe.app.domain.board.QAttachmentFile>createList("attachmentFiles", project.studycafe.app.domain.board.AttachmentFile.class, project.studycafe.app.domain.board.QAttachmentFile.class, PathInits.DIRECT2);

    public QBoardAddInfo(String variable) {
        super(BoardAddInfo.class, forVariable(variable));
    }

    public QBoardAddInfo(Path<? extends BoardAddInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardAddInfo(PathMetadata metadata) {
        super(BoardAddInfo.class, metadata);
    }

}

