package project.studycafe.repository.board.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import project.studycafe.domain.Board;
import project.studycafe.repository.board.board.dto.BoardSearchCond;

import javax.persistence.EntityManager;
import java.util.List;

import static project.studycafe.domain.QBoard.board;

public class JpaQueryBoardRepository {
    private final JPAQueryFactory query;

    public JpaQueryBoardRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    // 기본이 최신순임.
    public List<Board> findSearchedAndSortedBoards(BoardSearchCond cond) {
        return query.select(board)
                .from(board)
                .where(
                        likeBoardTitle(cond.getTitle()),
//                        likeBoardCreatedUserName(cond.getUserName()),
                        eqBoardCategory(cond.getCategory())
                )
                .orderBy(
                        sortedBoardBySort(cond.getSort()),
                        board.createdTime.desc()
                )
                .fetch();
    }

    public OrderSpecifier<?> sortedBoardBySort(String sort) {
        if (StringUtils.hasText(sort)) {
            if ("readCount".equalsIgnoreCase(sort)) {
                return board.readCount.desc();
            } else if ("likeCount".equalsIgnoreCase(sort)) {
                return board.likeCount.desc();
            }
        }
        return board.createdTime.desc();
    }

    private BooleanExpression likeBoardTitle(String boardName) {
        if (StringUtils.hasText(boardName)) {
            return board.title.like("%" + boardName + "%");
        }
        return null;
    }

//    private BooleanExpression likeBoardCreatedUserName(String userName) {
//        if (StringUtils.hasText(userName)) {
//            return board.member.like("%" + userName + "%");
//        }
//        return null;
//    }

    private Predicate eqBoardCategory(String category) {
        if (StringUtils.hasText(category)) {
            return board.category.eq(category);
        }
        return board.category.eq("커뮤니티");
    }


}
