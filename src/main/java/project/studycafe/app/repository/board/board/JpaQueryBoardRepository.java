package project.studycafe.app.repository.board.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import project.studycafe.app.domain.board.AttachmentFile;
import project.studycafe.app.domain.board.Board;
import project.studycafe.app.service.dto.searchDto.BoardSearchCond;

import javax.persistence.EntityManager;
import java.util.List;

import static project.studycafe.app.domain.board.QBoard.board;
import static project.studycafe.app.domain.board.QComment.comment;
import static project.studycafe.app.domain.member.QMember.member;


public class JpaQueryBoardRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaQueryBoardRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Board> findAllWithMemberCommentReply() {
        return em.createQuery(
                "select b from Board b" +
                        " join fetch b.member m" +
                        " join fetch b.comments c" +
                        " join fetch c.replies r", Board.class
        ).getResultList();
    }

    public Board findByIdWithMemberComment(long boardId) {
        return em.createQuery(
                        "select b from Board b" +
                                " join fetch b.member m" +
                                " join fetch b.comments c" +
                                " where b.id = :boardId", Board.class
                ).setParameter("boardId", boardId).
                getSingleResult();
    }
    public Board findByIdWithMemberCommentByQuery(long boardId) {
        return query
                .selectFrom(board)
                .join(board.member, member).fetchJoin()
                .join(board.comments, comment).fetchJoin()
                .where(board.id.eq(boardId))
                .fetchOne();
    } //JPA에서 Fetch Join의 조건은 다음과 같다.

    //ToOne은 몇개든 사용 가능
    //ToMany는 1개만 가능

    public List<AttachmentFile> getAttachmentFilesByBoardId(long boardId) {
        return em.createQuery(
                        "select a from AttachmentFile a" +
                                " where a.board.id = :boardId", AttachmentFile.class
                ).setParameter("boardId", boardId).
                getResultList();
    }






    // 기본이 최신순임.
    public List<Board> findSearchedAndSortedBoards(BoardSearchCond cond) {
        return query.select(board)
                .from(board)
                .where(
                        likeBoardTitle(cond.getTitle()),
                        likeCreatedMemberNickName(cond.getUserNickname()),
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
            if ("boardReadCountUp".equalsIgnoreCase(sort)) {
                return board.statistics.readCount.asc();
            } else if ("boardReadCountDown".equalsIgnoreCase(sort)) {
                return board.statistics.readCount.desc();
            } else if ("boardLikeCountUp".equalsIgnoreCase(sort)) {
                return board.statistics.likeCount.asc();
            } else if ("boardLikeCountDown".equalsIgnoreCase(sort)) {
                return board.statistics.likeCount.desc();
            }
        }
        return board.createdTime.desc();
    }

    private BooleanExpression likeBoardTitle(String boardName) {
        if (StringUtils.hasText(boardName)) {
            return board.boardBaseInfo.title.like("%" + boardName + "%");
        }
        return null;
    }

    private Predicate likeCreatedMemberNickName(String userNickname) {
        if (StringUtils.hasText(userNickname)) {
            return board.member.nickname.like("%" + userNickname + "%");
        }
        return null;
    }

    private Predicate eqBoardCategory(String category) {
        if (StringUtils.hasText(category)) {
            return board.boardBaseInfo.category.eq(category);
        }
        return board.boardBaseInfo.category.ne("공지사항");
    }
}
