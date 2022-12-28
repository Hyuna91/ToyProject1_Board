package com.example.board.repository.search;

import com.example.board.entity.Board;
import com.example.board.entity.QBoard;
import com.example.board.entity.QMember;
import com.example.board.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

// SearchBoardRepository의 구현부
// QuerydslRepositorySupport 클래스는 Querydsl 라이브러리를 이용하여 직접 구현을 할 때 사용
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

    public SearchBoardRepositoryImpl() {

        // QuerydslRepositorySupport 클래스를 상속받으므로 super() 사용
        // super() : 서브(자식) 클래스에서 상위 클래스를 호출할 때 사용
        super(Board.class);

    }

    @Override
    public Board search1() {

        log.info("search1................");

        // 동적으로 처리하기 위해서 Q도메인 클래스를 얻어온다.
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        // Querydsl 라이브러리 내에 JPQLQuery 인터페이스 활용
        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        // Board와 Reply을 Left Outer(.on 사용) Join으로 연결
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // 1. 정해진 엔티티(select(board))를 가져오는 경우
//        jpqlQuery.select(board).where(board.bno.eq(1L));

        // 2. 각각의 데이터(select(board, member.email, reply.count()))를 추출하기 위해선 tuple 객체를 사용
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("---------------------------------");
        log.info(jpqlQuery);    // select board
                                // from Board board
                                // where board.bno = ?1
        log.info("---------------------------------");

        // 1. 정해진 엔티티(select(board))를 가져오는 경우
//        List<Board> result = jpqlQuery.fetch();

        // 2. 각각의 데이터(select(board, member.email, reply.count()))를 추출하기 위해선 tuple 객체를 사용
        List<Tuple> result = tuple.fetch();

        return null;

    }

    /** 검색 조건의 처리 */
    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage..........");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null) {

            String[] typeArr = type.split("");

            // 검색 조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr) {
                switch (t) {
                    case "t" :
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w" :
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c" :
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        /** Sort 처리 : Order By */
        // Pageable에서 sort를 가져온다.
        Sort sort = pageable.getSort();

        // 직접 코드로 처리하는 방법
//        tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            // 오른차순인지 내림차순인지 확인
            Order direction = order.isAscending()? Order.ASC: Order.DESC;

            // getProperty()는 orderBy(board.bno.desc())에서 bno를 가져오는 역할
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(board);

        /** Page 처리 */
        tuple.offset(pageable.getOffset()); // 정렬 매개 변수를 반환
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT : " + count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),pageable, count
        );
    }
}
