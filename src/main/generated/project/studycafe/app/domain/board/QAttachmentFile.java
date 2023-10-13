package project.studycafe.app.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttachmentFile is a Querydsl query type for AttachmentFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttachmentFile extends EntityPathBase<AttachmentFile> {

    private static final long serialVersionUID = 2145498727L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttachmentFile attachmentFile = new QAttachmentFile("attachmentFile");

    public final project.studycafe.app.domain.base.QBaseTimeEntity _super = new project.studycafe.app.domain.base.QBaseTimeEntity(this);

    public final StringPath attachmentFileName = createString("attachmentFileName");

    public final NumberPath<Long> attachmentFileSize = createNumber("attachmentFileSize", Long.class);

    public final EnumPath<project.studycafe.app.domain.enums.FileType> attachmentFileType = createEnum("attachmentFileType", project.studycafe.app.domain.enums.FileType.class);

    public final QBoard board;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final StringPath uniqueFileName = createString("uniqueFileName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QAttachmentFile(String variable) {
        this(AttachmentFile.class, forVariable(variable), INITS);
    }

    public QAttachmentFile(Path<? extends AttachmentFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttachmentFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttachmentFile(PathMetadata metadata, PathInits inits) {
        this(AttachmentFile.class, metadata, inits);
    }

    public QAttachmentFile(Class<? extends AttachmentFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

