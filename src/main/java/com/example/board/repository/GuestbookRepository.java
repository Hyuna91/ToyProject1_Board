package com.example.board.repository;

import com.example.board.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

// Querydsl을 이용하기위해 QuerydslPredicateExecutor<Guestbook>를 추가 상속
public interface GuestbookRepository extends JpaRepository<Guestbook, Long>,
        QuerydslPredicateExecutor<Guestbook> {
}
