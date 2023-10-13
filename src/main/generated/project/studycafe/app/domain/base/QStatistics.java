package project.studycafe.app.domain.base;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatistics is a Querydsl query type for Statistics
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QStatistics extends BeanPath<Statistics> {

    private static final long serialVersionUID = -915105920L;

    public static final QStatistics statistics = new QStatistics("statistics");

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final NumberPath<Integer> readCount = createNumber("readCount", Integer.class);

    public QStatistics(String variable) {
        super(Statistics.class, forVariable(variable));
    }

    public QStatistics(Path<? extends Statistics> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatistics(PathMetadata metadata) {
        super(Statistics.class, metadata);
    }

}

