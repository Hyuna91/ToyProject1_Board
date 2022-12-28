package com.example.board.repository.search;

import com.example.board.dto.PageRequestDTO;
import com.example.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Repository의 확장
public interface SearchBoardRepository {
    
    // Board 타입의 객체를 반환하는 메서드를 선언
    Board search1();

    // 검색 타입과 키워드, 페이지 정보를 파라미터를 넣어서 처리
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
