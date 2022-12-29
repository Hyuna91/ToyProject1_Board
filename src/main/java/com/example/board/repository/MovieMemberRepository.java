package com.example.board.repository;

import com.example.board.entity.MovieMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieMemberRepository extends JpaRepository<MovieMember, Long> {
}
