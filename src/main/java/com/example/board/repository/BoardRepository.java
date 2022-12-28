package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    /** 엔티티 클래스 내부에 연관관계가 있는 경우 */
    /** JPQL을 사용하여 FetchType.LAZY를 사용해도 조인 처리가 되어 한 번에 board, member 테이블을 사용할 수 있다. */
    /**
     * 한개의 로우(Object)내에 Object[]로 나옴
     */
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);


    /**
     * 엔티티 클래스 내부에 연관관계가 없는 경우 : on
     */
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);


    /** 목록화면 : Table 3개 Join하기 */
    @Query(value = "select b, w, count(r) FROM Board b left join b.writer w left join Reply r on r.board = b Group by b",
            countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);


    /** 조회화면 : 특정 게시물 번호를 사용 */
    @Query("select b, w, count(r) from Board b left join b.writer w left outer join Reply r on r.board = b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
