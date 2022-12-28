package com.example.board.repository;

import com.example.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    /** 특정 bno로 댓글을 삭제 : 해당 게시물의 댓글을 삭제하는 기능 */
    /** JPQL을 이용해 Update나 Delete를 하는 경우 @Modifying를 추가해야 한다. */
    @Modifying
    @Query("delete from Reply r where r.board.bno =:bno ")
    void deleteByBno(Long bno);

}
